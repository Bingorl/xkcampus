package com.biu.wifi.component.huanxin;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.support.cache.ehcache.CacheTool;
import com.biu.wifi.core.support.exception.ServiceException;
import com.biu.wifi.core.util.HttpClientUtils;
import com.biu.wifi.core.util.JSONUtilsEx;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * 环信 <br>
 * 用户密码加密在服务器端
 *
 * @author HJD
 */
@Service
public class HuanXinService {
    private static JsonNodeFactory factory = new JsonNodeFactory(false);

    /**
     * 注册IM用户
     *
     * @param userPrimaryKey
     * @param dataObjectNode
     * @return
     * @throws Exception
     */
    public String addImMembers(String userName, String passWord, String nickName) throws Exception {

        ObjectNode objectNode1 = factory.objectNode();
        ObjectNode objectNode = factory.objectNode();
        HttpClient httpClient = HTTPClientUtils.getClient(true);
        HashMap<String, Object> map = new HashMap<String, Object>();
        String hxName = null;
        // 获得环信的token值
        //String token = getToken();
        try {
            URL allMemberssByGroupIdUrl = HTTPClientUtils.getURL(CoreConstants.getProperty("hx_app_name").replace("#", "/") + "/users");
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(allMemberssByGroupIdUrl.toURI());
            List<NameValuePair> headers = new ArrayList<NameValuePair>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));

            if (null != headers && !headers.isEmpty()) {
                for (NameValuePair nameValuePair : headers) {
                    httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
            }
            objectNode1.put("username", userName);
            // Base64加密
            // objectNode1.put("username",(new BASE64Encoder()).encodeBuffer(userName.getBytes()));
            objectNode1.put("password", passWord);
            objectNode1.put("nickname", nickName);

            httpPost.setEntity(new StringEntity(objectNode1.toString(), "UTF-8"));
            HttpResponse tokenResponse = httpClient.execute(httpPost);
            HttpEntity entity = tokenResponse.getEntity();

            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(responseContent);
                objectNode = mapper.readTree(jp);

                map = JSONUtilsEx.deserialize(objectNode.toString(), HashMap.class);
                System.out.println("------" + map);
                // error=duplicate_unique_property_exists
                if (map.get("error") != null
                        && map.get("error").toString().equals("duplicate_unique_property_exists")) {
                    //throw new ServiceException("此用户已被注册");
                } else {
                    if (map.get("entities") != null) {
                        hxName = ((Map<?, ?>) ((List<?>) map.get("entities")).get(0)).get(
                                "username").toString();
                    } else {
                        //throw new ServiceException("此用户已被注册");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return hxName;
    }

    /**
     * 更新昵称
     *
     * @param userName
     * @param nickName
     */
    public void updateImMemberName(String userName, String nickName) {
        ObjectNode objectNode = factory.objectNode();
        ObjectNode objectNode1 = factory.objectNode();
        HttpClient httpClient = HTTPClientUtils.getClient(true);
        HashMap<String, Object> map = new HashMap<String, Object>();
        String token = getToken();
        try {
            URL allMemberssByGroupIdUrl = HTTPClientUtils.getURL(CoreConstants.getProperty("hx_app_name").replace("#", "/") + "/users/" + userName);
            HttpPut httpPut = new HttpPut();
            httpPut.setURI(allMemberssByGroupIdUrl.toURI());
            List<NameValuePair> headers = new ArrayList<NameValuePair>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));
            headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
            if (null != headers && !headers.isEmpty()) {
                for (NameValuePair nameValuePair : headers) {
                    httpPut.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
            }

            objectNode1.put("nickname", nickName);
            httpPut.setEntity(new StringEntity(objectNode1.toString(), "UTF-8"));
            HttpResponse tokenResponse = httpClient.execute(httpPut);
            HttpEntity entity = tokenResponse.getEntity();

            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(responseContent);
                objectNode = mapper.readTree(jp);

                map = JSONUtilsEx.deserialize(objectNode.toString(), HashMap.class);
                System.out.println("------" + map);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * 用户强制下线
     *
     * @param userName
     */
    public void disConnect(String userName) {
        ObjectNode objectNode = factory.objectNode();
        HttpClient httpClient = HTTPClientUtils.getClient(true);
        HashMap<String, Object> map = new HashMap<String, Object>();
        String token = getToken();
        try {
            URL allMemberssByGroupIdUrl = HTTPClientUtils.getURL(CoreConstants.getProperty("hx_app_name").replace("#", "/") + "/users/" + userName + "/disconnect");
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(allMemberssByGroupIdUrl.toURI());
            List<NameValuePair> headers = new ArrayList<NameValuePair>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));
            headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
            if (null != headers && !headers.isEmpty()) {
                for (NameValuePair nameValuePair : headers) {
                    httpGet.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
            }
            HttpResponse tokenResponse = httpClient.execute(httpGet);
            HttpEntity entity = tokenResponse.getEntity();

            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(responseContent);
                objectNode = mapper.readTree(jp);

                map = JSONUtilsEx.deserialize(objectNode.toString(), HashMap.class);
                System.out.println("------" + map);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * 删除IM用户
     *
     * @param userPrimaryKey
     * @param dataObjectNode
     * @return
     * @throws Exception
     */
    public void delImMembers(String userName) {
        ObjectNode objectNode = factory.objectNode();
        HttpClient httpClient = HTTPClientUtils.getClient(true);
        HashMap<String, Object> map = new HashMap<String, Object>();
        String token = getToken();
        try {
            URL allMemberssByGroupIdUrl = HTTPClientUtils.getURL(CoreConstants.getProperty("hx_app_name").replace("#", "/") + "/users/" + userName);
            HttpDelete httpDelete = new HttpDelete();
            httpDelete.setURI(allMemberssByGroupIdUrl.toURI());
            List<NameValuePair> headers = new ArrayList<NameValuePair>();
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));
            headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
            if (null != headers && !headers.isEmpty()) {
                for (NameValuePair nameValuePair : headers) {
                    httpDelete.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
            }
            HttpResponse tokenResponse = httpClient.execute(httpDelete);
            HttpEntity entity = tokenResponse.getEntity();

            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(responseContent);
                objectNode = mapper.readTree(jp);

                map = JSONUtilsEx.deserialize(objectNode.toString(), HashMap.class);
                System.out.println("------" + map);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    /**
     * 提供token
     *
     * @return
     */
    public String getToken() {
        String token = CacheTool.get(110);
        //String token = "YWMtnf1s3AvGEei-rhPmhR6a1wAAAAAAAAAAAAAAAAAAAAFMNBfQC6IR6JtbeQc2skkMAgMAAAFhbrVPxgBPGgA8aC0KC7LXnMGLNOs-hPd_eA3z9LGI4P1rgO37wqgNCg";
        if (StringUtils.isBlank(token)) {
            if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+",
                    CoreConstants.getProperty("hx_app_name"))) {
                // LOGGER.error("Bad format of Constants.APPKEY: " +
                // Constants.APPKEY);
            }
            HashMap<String, String> map = new HashMap<String, String>();
            try {
                Map<String, String> qqParams = new HashMap<String, String>();

                // 需要传给的参数 换取access token
                qqParams.put("client_id", CoreConstants.getProperty("hx_client_id"));
                qqParams.put("client_secret", CoreConstants.getProperty("hx_client_secret"));
                qqParams.put("grant_type", CoreConstants.getProperty("hx_grant_type"));
                String ret = HttpClientUtils.get("https://" + CoreConstants.getProperty("hx_org_name")
                                + "/" + CoreConstants.getProperty("hx_app_name").replace("#", "/") + "/token",
                        qqParams);
                System.out.println("环信返回值：" + ret);
                map = JSONUtilsEx.deserialize(ret, HashMap.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // httpClient.getConnectionManager().shutdown();
            }
            token = map.get("access_token").toString();
            CacheTool.put(110, token, 7000);
        } else {
            System.out.println("直接缓存拿~~~~~" + token);
        }
        return token;

    }


    /**
     * @param @param  id
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: hxToId
     * @Description: TODO(hxId转UUID)
     * @作者 韩建东
     * @date 2015-8-5 上午9:01:08
     */
    public String hxToId(String id) {
        StringBuffer sb = new StringBuffer(id);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new HuanXinService().getToken());
        //new HuanXinService().updateImMemberName("18052711978", "xuyue");
        //System.out.println(new HuanXinService().addImMembers("18052711978", "123456", "xy"));
        //new HuanXinService().disConnect("18052711978");
    }
}
