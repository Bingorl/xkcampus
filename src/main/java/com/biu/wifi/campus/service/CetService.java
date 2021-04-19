package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.HttpRequest;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.dao.CetScoreMapper;
import com.biu.wifi.campus.dao.model.CetScore;
import com.biu.wifi.campus.dao.model.School;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangbin.
 * @date 2018/10/12.
 */
@Service
public class CetService {

    @Autowired
    private CetScoreMapper cetScoreMapper;
    @Autowired
    private SchoolService schoolService;

    /**
     * 中国高等教育学生信息网（学信网）首页网址
     */
    public static String website = "https://www.chsi.com.cn/";

    /**
     * 认证参数
     */
    public static String referer = "https://www.chsi.com.cn/cet/";

    /**
     * 获取验证码接口
     */
    public static String authCodeApi = "https://www.chsi.com.cn/cet/ValidatorIMG.JPG?ID=%s";

    /**
     * 四六级查询接口
     */
    public static String cetQueryApi = "https://www.chsi.com.cn/cet/query?zkzh=%s&xm=%s&yzm=%s";

    /**
     * 学信网的sessionId
     */
    public String JSESSIONID = null;

    /**
     * 登陆学信网首页
     */
    public void login() {
        HttpsURLConnection httpsURLConnection = HttpRequest.createHttpsURLConnection(website, "GET", null);
        System.out.println(HttpRequest.getHtml(httpsURLConnection));
    }

    /**
     * 获取验证码
     */
    public Map<String, String> getAuthCodeUrl() {
        String url = String.format(authCodeApi, System.currentTimeMillis());
        Map requestProperties = new HashMap<>();
        requestProperties.put("Referer", referer);
        HttpsURLConnection httpsURLConnection = HttpRequest.createHttpsURLConnection(url, "GET", requestProperties);
        try {
            String setCookie = httpsURLConnection.getHeaderField("Set-Cookie");
            if (setCookie.contains("JSESSIONID")) {
                //将cookie中的jsessionid保存
                JSESSIONID = setCookie.substring(0, setCookie.indexOf(";"));
            }
            InputStream inputStream = httpsURLConnection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            String fileName = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + ".jpg";
            fileName = NginxFileUtils.add(fileName, bos.toByteArray(), "ds_upload", null);
            fileName = fileName.replace("\\\\", "/");

            Map<String, String> data = new HashMap<>();
            data.put("fileName", fileName);
            data.put("JSESSIONID", JSESSIONID);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取四六级查询结果页面
     *
     * @param exCardNum 准考证号
     * @param realName  姓名
     * @param authCode  验证码
     */
    public String getCetQueryHtml(String exCardNum, String realName, String authCode, String sessionId) {
        String url = String.format(cetQueryApi, exCardNum, HttpRequest.urlEncodeUTF8(realName), HttpRequest.urlEncodeUTF8(authCode));
        Map requestProperties = new HashMap<>();
        requestProperties.put("Referer", referer);
        requestProperties.put("Upgrade-Insecure-Requests", "1");
        requestProperties.put("Cookie", sessionId);
        try {
            HttpsURLConnection httpsURLConnection = HttpRequest.createHttpsURLConnection(url, "GET", requestProperties);
            String page = HttpRequest.getHtml(httpsURLConnection);
            System.out.println(page);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过对html页面进行解析获取到有用数据
     *
     * @param html
     * @return
     */
    public Result getCetResult(String html, Integer schoolId, String exCardNum) {
        if (StringUtils.isBlank(html)) {
            return new Result(Result.CUSTOM_MESSAGE, "请先获取验证码", null);
        }
        Document doc = Jsoup.parse(html);
        Elements tableList = doc.getElementsByClass("cetTable");
        Elements divList = doc.getElementsByClass("error");
        //验证码不正确 div class = "error alignC"
        //无法找到对应的分数，请确认您输入的准考证号及姓名无误。 div class = "error alignC marginT20"
        if (divList.size() > 0) {
            Element div = divList.get(0);
            Set<String> set = div.classNames();
            if (set.contains("marginT20")) {
                return new Result(Result.CUSTOM_MESSAGE, div.text(), "");
            } else {
                return new Result(Result.CUSTOM_MESSAGE, div.text(), "invalid_code");
            }
        } else if (tableList.size() > 0) {
            //解析考试成绩
            Map data = new HashMap();
            //考试时间
            Elements h2 = doc.getElementsByTag("h2");
            if (h2.size() > 0) {
                data.put("cetExamTime", h2.get(1).text().substring(0, 8));
            } else {
                data.put("cetExamTime", "");
            }
            //准考证号
            data.put("exCardNum", exCardNum);
            Element table = tableList.get(0);
            Elements trList = table.getElementsByTag("tr");
            Element tr, td;
            //考试级别
            String cet = trList.get(2).getElementsByTag("td").get(0).text();
            data.put("cet", cet);
            //笔试成绩
            String totalScore = trList.get(5).getElementsByTag("td").get(0).getElementsByTag("span").get(0).text();
            data.put("totalScore", totalScore);
            //听力成绩
            String listenScore = trList.get(6).getElementsByTag("td").get(1).text();
            data.put("listenScore", listenScore);
            //阅读成绩
            String readScore = trList.get(7).getElementsByTag("td").get(1).text();
            data.put("readScore", readScore);
            //写作和翻译成绩
            String writingScore = trList.get(8).getElementsByTag("td").get(1).text();
            data.put("writingScore", writingScore);
            //口试成绩
            String oralTestScore = trList.get(11).getElementsByTag("td").get(0).text();
            data.put("oralTestScore", oralTestScore);

            //保存考试成绩
            CetScore cetScore = new CetScore();
            cetScore.setExCardNum(exCardNum);
            cetScore.setSchoolId(schoolId);
            School school = schoolService.getById(schoolId);
            cetScore.setSchoolName(school.getName());
            cetScore.setCet(data.get("cet").toString());
            cetScore.setTotalScore(data.get("totalScore").toString());
            cetScore.setListenScore(data.get("listenScore").toString());
            cetScore.setReadScore(data.get("readScore").toString());
            cetScore.setWritingScore(data.get("writingScore").toString());
            cetScore.setOralTestScore(data.get("oralTestScore").toString());
            cetScore.setCetExamTime(data.get("cetExamTime").toString());
            cetScoreMapper.insertSelective(cetScore);

            return new Result(Result.SUCCESS, "成功", data);
        }

        return new Result(Result.FAILURE, "失败", null);
    }
}
