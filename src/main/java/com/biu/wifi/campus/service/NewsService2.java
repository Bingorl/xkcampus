package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.FileKit;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.dao.NewsMapper;
import com.biu.wifi.campus.dao.model.News;
import com.biu.wifi.campus.dao.model.NewsCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.support.exception.ServiceException;
import com.biu.wifi.core.util.SystemUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2019/3/12.
 */
@Service
public class NewsService2 {

    /**
     * 河海大学官网地址的前缀
     */
    // 常州大学总校区
    //private static final String url_prefix = "http://www.hhu.edu.cn";
    // 常州大学常州校区
    private static final String url_prefix = "http://www.hhuc.edu.cn";
    /**
     * 河海大学重要新闻新闻页面的链接
     */
    //private static final String newsUrl = "http://www.hhu.edu.cn/166/list.htm";
    //private static final String newsUrlFormat = "http://www.hhu.edu.cn/166/list%s.htm";
    private static final String newsUrl = "http://www.hhuc.edu.cn/443/list.htm";
    private static final String newsUrlFormat = "http://www.hhuc.edu.cn/443/list%s.htm";

    /**
     * 河海大学视频新闻页面的链接
     */
    //private static final String newsNoticeUrl = "http://www.hhu.edu.cn/29/list.htm";
    //private static final String newsNoticeUrlFormat = "http://www.hhu.edu.cn/29/list%s.htm";
    private static final String vedioNewsUrl = "http://www.hhuc.edu.cn/444/list.htm";
    private static final String vedioNewsUrlFormat = "http://www.hhuc.edu.cn/444/list%s.htm";

    /**
     * 河海大学图片新闻页面的链接
     */
    private static final String imageNewsUrl = "http://www.hhuc.edu.cn/445/list.htm";
    private static final String imageNewsUrlFormat = "http://www.hhuc.edu.cn/445/list%s.htm";

    /**
     * 超时时间(1min)
     */
    private int timeout = 1 * 60 * 1000;

    @Autowired
    private NewsMapper newsMapper;

    public void add(int schoolId, String title, String dateStr, String content) {
        Date publishTime = null;
        try {
            publishTime = DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd"});
        } catch (Exception e) {
        }
        News news = new News();
        news.setSchoolId(schoolId);
        news.setTitle(title);
        news.setContent(content);
        news.setPublishTime(publishTime);
        news.setCreateTime(new Date());
        newsMapper.insertSelective(news);
    }

    public List<News> newsList(int schoolId, String keyword, String startTime, String endTime) {
        NewsCriteria example = new NewsCriteria();
        example.setOrderByClause("publish_time desc,id desc");
        NewsCriteria.Criteria criteria = example.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andTitleLike("%" + keyword + "%");
        }

        String[] patters = new String[]{"yyyy-MM-dd HH:mm:ss"};
        if (StringUtils.isNotBlank(startTime)) {
            Date date = null;
            try {
                date = DateUtils.parseDate(startTime + " 00:00:00", patters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            criteria.andPublishTimeGreaterThanOrEqualTo(date);
        }

        if (StringUtils.isNotBlank(endTime)) {
            Date date = null;
            try {
                date = DateUtils.parseDate(endTime + " 00:59:59", patters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            criteria.andPublishTimeLessThanOrEqualTo(date);
        }
        return newsMapper.selectByExampleWithBLOBs(example);
    }

    public News parseHtmlToGetNews(int schoolId, int type, String linkUrl) throws IOException {
        Document document = createDocument(linkUrl);
        // 标题
        Elements titleEle = document.getElementsByClass("Article_Title");
        // 发布时间
        Elements publishTimeEle = document.getElementsByClass("posttime");
        Elements contentEle = document.getElementsByClass("infobox");
        Date publishTime = null;
        try {
            String timeStr = publishTimeEle.text();// 发布时间：2019-04-03
            publishTime = DateUtils.parseDate(timeStr.substring(5, timeStr.length()), new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 处理图片地址
        List<String> picList = new ArrayList<>();
        contentEle = parseImageUrlLink(contentEle, picList);

        String title = titleEle.text();
        String content = contentEle.html();
        System.out.println("解析新闻成功");
//        return null;
        NewsCriteria example = new NewsCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andTypeEqualTo(type)
                .andLinkUrlEqualTo(linkUrl);
        List<News> newsList = newsMapper.selectByExample(example);

        if (newsList.isEmpty()) {
            News news = new News();
            news.setSchoolId(schoolId);
            news.setType(type);
            news.setTitle(title);
            news.setPublishTime(publishTime);
            news.setContent(content);
            news.setLinkUrl(linkUrl);

            if (picList.size() > 0) {
                news.setPic(StringUtils.join(picList, ","));
            }
            newsMapper.insertSelective(news);
            return news;
        } else {
            return newsList.get(0);
        }
    }

    /**
     * 解析文档中的图片,将相对路径转换成绝对路径
     *
     * @param elements
     * @return
     */
    public Elements parseImageUrlLink(Elements elements, List<String> picList) {
        //String rootPath = "/usr/local/nginx/html/campus/";
        String rootPath = "D:\\Java Project\\开发环境静态资源路径\\";
        String serverPath = "https://app.54xy.com/campus";

        Elements medias = elements.select("[src]");
        for (Element src : medias) {
            if (src.tagName().equals("img")) {
                String url = src.attr("src");
                if (StringUtils.isNotBlank(url) && !StringUtil.isUrl(url)) {
                    // 转存图片到服务器
                    try {
                        String absoluteUrl = url_prefix + url;
                        String pic = FileKit.write(absoluteUrl, rootPath);
                        // 设置图片的全路径
                        src.attr("src", serverPath + "/" + FileKit.now + "/" + pic);
                        picList.add(FileKit.now + "/" + pic);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new ServiceException(Result.CUSTOM_MESSAGE, "图片转存失败");
                    }
                }
            }
        }
        return elements;
    }

    /**
     * 解析到总页数
     *
     * @return
     * @throws IOException
     */
    public int getNewsPageTotal(String newsPage) throws IOException {
        Document document = createDocument(newsPage);
        Elements elements = document.getElementsByClass("all_pages");
        System.out.println(elements.text());
        return Integer.valueOf(elements.text());
    }

    /**
     * 解析新闻页面的a链接的href地址
     *
     * @throws IOException
     */
    public List<String> getNewsLinkUrl(String pageUrl) throws IOException {
        List<String> list = new ArrayList<>();
        Document document = createDocument(pageUrl);
        Elements links = document.select("a[href]");
        for (Element href : links) {
            if (href.tagName().equals("a")) {
                String hrefUrl = href.attr("href");
                // 去除首页、尾页和脚本跳转的href
                if (!Arrays.asList("first", "prev", "next", "last", "pagingJump").contains(href.className())
                        && hrefUrl.startsWith("/2019/")) {
                    System.out.println(hrefUrl);
                    list.add(url_prefix + hrefUrl);
                }
            }
        }
        return list;
    }

    private Document createDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(timeout)
                .header("Host", "www.hhuc.edu.cn")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                .get();
    }

    /**
     * 批量导入新闻
     *
     * @throws IOException
     */
    public void importNews(Integer schoolId) throws IOException {
        int totalPages;
        // 重要新闻列表
        totalPages = getNewsPageTotal(newsUrl);
        for (int i = 1; i <= totalPages; i++) {
            List<String> newsLinkList = getNewsLinkUrl(String.format(newsUrlFormat, String.valueOf(i)));
            System.out.println("重要新闻列表当前是第" + i + "页");
            for (String link : newsLinkList)
                parseHtmlToGetNews(schoolId, 1, link);
        }

        // 视频新闻列表
        totalPages = getNewsPageTotal(vedioNewsUrl);
        for (int i = 1; i <= totalPages; i++) {
            List<String> newsLinkList = getNewsLinkUrl(String.format(vedioNewsUrlFormat, String.valueOf(i)));
            System.out.println("视频新闻列表当前是第" + i + "页");
            for (String link : newsLinkList)
                parseHtmlToGetNews(schoolId, 2, link);
        }

        // 图片新闻列表
        totalPages = getNewsPageTotal(imageNewsUrl);
        for (int i = 1; i <= totalPages; i++) {
            List<String> newsLinkList = getNewsLinkUrl(String.format(imageNewsUrl, String.valueOf(i)));
            System.out.println("视频新闻列表当前是第" + i + "页");
            for (String link : newsLinkList)
                parseHtmlToGetNews(schoolId, 3, link);
        }
    }

    public static void main(String[] args) throws IOException {
//        NewsService2 newsService = new NewsService2();
//        newsService.importNews(26);

        Document document=Jsoup.connect("http://www.xhkjzyxy.cn/news/html1/?2063.html")
                .timeout(10000)
//                .header("Host", "www.hhuc.edu.cn")
//                .header("Upgrade-Insecure-Requests", "1")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                .get();

        System.out.println(document.body());
    }

    public News findById(Integer id, Integer schoolId) {
        NewsCriteria example = new NewsCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIdEqualTo(id)
                .andSchoolIdEqualTo(schoolId);
        List<News> newsList = newsMapper.selectByExampleWithBLOBs(example);
        if (newsList.isEmpty())
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录不存在");
        else
            return newsList.get(0);
    }

    public News findNextByPreNews(News current) {
        NewsCriteria example = new NewsCriteria();
        example.setOrderByClause("publish_time desc,id desc");
        example.createCriteria()
                .andPublishTimeLessThanOrEqualTo(current.getPublishTime());

        List<News> newsList = newsMapper.selectByExampleWithBLOBs(example);
        if (!newsList.isEmpty()) {
            int length = newsList.size();
            for (int i = 0; i < length; i++) {
                if (current.getId().intValue() == newsList.get(i).getId().intValue()) {
                    if (i != length - 1) {
                        return newsList.get(i + 1);
                    }
                }
            }
        }

        return null;
    }

    @Async
    public void addClickCount(News news, HttpServletRequest request) {
        String ip = SystemUtilsEx.getClientIpSingle(request);
        String key = new StringBuffer("news_")
                .append(news.getId())
                .append("_viewer:")
                .append(ip)
                .toString();

        /*if (RedisUtils.getValueByKey(key) == null) {*/
        news.setClickCount(news.getClickCount() + 1);
        newsMapper.updateByPrimaryKeySelective(news);
        RedisUtils.addValue(key, key, 10 * 60);
        /*}*/
    }


    /**
     * 替换新闻内容中的图片路径
     */
    public void replaceImagePath() {
        NewsCriteria example = new NewsCriteria();
        example.setOrderByClause("publish_time desc");
        example.createCriteria()
                .andIdGreaterThan(144);
        List<News> newsList = newsMapper.selectByExampleWithBLOBs(example);
        String serverPath = "https://app.54xy.com/campus";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (News news : newsList) {
//            String full = serverPath + "/" + sdf.format(news.getPublishTime()) + "/";
//            boolean has = news.getContent().contains(serverPath);
//            boolean none = !news.getContent().contains(full);
//            System.out.println(news.getContent());
//            if (has && none) {
//                news.setContent(news.getContent().replaceAll(serverPath, full));
//                newsMapper.updateByPrimaryKeySelective(news);
//            }
            if (news.getContent().contains("/" + sdf.format(news.getPublishTime()) + "/")) {
                news.setContent(news.getContent().replaceAll("/" + sdf.format(news.getPublishTime()) + "/", "/" + sdf.format(new Date()) + "/"));
                newsMapper.updateByPrimaryKeySelective(news);
            }

        }
    }
}
