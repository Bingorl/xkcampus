package com.biu.wifi.campus.Tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class BehaviorImageUtils {
	
	private static final String APP_CODE = "dd8421a19f1442baac235cc5f3485143";
	private static final String APP_KEY = "73d0b075c3882194f2a162b43da800d8";
	
//	 "EDUCATION_JUNIOR":"EDUCATION_01",              #初中及以下，如：初中及以下以EDUCATION_01表示
//     "EDUCATION_HIGH":"EDUCATION_02",                #高中及以下，如：高中及以下以EDUCATION_02表示
//     "EDUCATION_MAJOR":"EDUCATION_03",               #大中专，如：大中专以EDUCATION_03表示
//     "EDUCATION_BACHELOR":"EDUCATION_04",            #本科，如：本科以EDUCATION_04表示
//     "EDUCATION_MASTER":"EDUCATION_05",              #硕士，如：硕士以EDUCATION_05表示
//     "EDUCATION_DOCTOR":"EDUCATION_06"               #博士，如：博士以EDUCATION_06表示
//     
//     "INCOME_LESS_THAN_3K":"INCOME_01",              #3000元以下，如：3000元以下以INCOME_01表示
//     "INCOME_3K_TO_5K":"INCOME_02",                  #3000-5000元，如：3000-5000元以INCOME_02表示
//     "INCOME_5K_TO_10K":"INCOME_03",                 #5000-10000元，如：5000-10000元以INCOME_03表示
//     "INCOME_10K_TO_20K":"INCOME_04",                #10000-20000元，如：10000-20000元以INCOME_04表示
//     "INCOME_20K_TO_30K":"INCOME_05",                #20000-30000元，如：20000-30000元以INCOME_05表示
//     "INCOME_30K_TO_50K":"INCOME_06",                #30000-50000元，如：30000-50000元以INCOME_06表示
//     "INCOME_MORE_THAN_50K":"INCOME_07"              #50000元以上，如：50000元以上以INCOME_07表示
	
	private static Log logger = LogFactory.getLog(BehaviorImageUtils.class);

	public static String doMBTI(String education, String idcode, String salary, String name, String phone) {

		String host = "http://mbti.market.alicloudapi.com";
	    String path = "/ai_mind/assessment/mbti/elite";
	    String method = "GET";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + APP_CODE);
	    
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("PERSON_BENCHMARK", "3");
	    querys.put("PERSON_EDUCATION", education);
	    querys.put("PERSON_ID", idcode);
	    querys.put("PERSON_INCOME", salary);
	    querys.put("PERSON_NAME", name);
	    querys.put("PERSON_PHONE", phone);

	    try {
	    	HttpResponse response = doGet(host, path, method, headers, querys);
	    	
	    	Header[] responseHeaders = response.getAllHeaders();
	    	
	    	for (Header header : responseHeaders) {
	    		if ("X-Ca-Error-Message".equals(header.getName())) {
	    			String val = header.getValue();
	    			
	    			if (StringUtils.isNotBlank(val)) {
	    				logger.info("doMBTI error : " + val);
	    				return "";
	    			}
	    		}
			}
	    	
	    	return EntityUtils.toString(response.getEntity());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return "";
	}
	
	public static void main(String[] args) {
		
		String result = doMBTI("EDUCATION_06", "320483199310217036", "INCOME_04", "李晓飞", "15851201425");
//		String result = "{\"PERSON_NAME\": \"李晓飞\", \"PERSON_SEX\": \"男性\", \"PERSON_ID\": \"320483199310217036\", \"PERSON_BIRTH\": \"1993年10月21日\", \"PERSON_CHINESE_BIRTH\": \"一九九三年十月廿一\", \"PERSON_SIGN\": \"天秤座\", \"PERSON_ANIMAL\": \"鸡\", \"PERSON_AGE_BANK\": \"21-25岁\", \"PERSON_INCOME\": \"10000-20000元\", \"PERSON_EDUCATION\": \"博士\", \"PERSON_BIRTH_COUNTRY_ID\": \"320483\", \"PERSON_BIRTH_COUNTRY_PROVINCE\": \"江苏省\", \"PERSON_BIRTH_CITY\": \"常州市\", \"PERSON_BIRTH_DISTRICT\": \"\", \"PERSON_MBTI_ENTITY\": {\"E_I_NET\": \"42.86%,42.86%,0.0%\", \"S_N_NET\": \"0.0%,85.71%,-85.71%\", \"T_F_NET\": \"85.71%,0.0%,85.71%\", \"J_P_NET\": \"57.14%,28.57%,28.57%\", \"MBTI-TYPE\": \"INTJ\", \"LEVEL\": 0}, \"MBTI_AGE_ENTITY\": {\"E_I_NET\": \"48.31%,51.69%,-3.38%\", \"S_N_NET\": \"52.53%,47.93%,4.6%\", \"T_F_NET\": \"51.96%,48.04%,3.92%\", \"J_P_NET\": \"32.91%,67.09%,-34.18%\", \"MBTI-TYPE\": \"ISTP\", \"LEVEL\": 1}, \"MBTI_EDUCATION_ENTITY\": {\"E_I_NET\": \"32.56%,67.44%,-34.88%\", \"S_N_NET\": \"42.28%,58.18%,-15.9%\", \"T_F_NET\": \"64.41%,35.59%,28.82%\", \"J_P_NET\": \"58.55%,41.45%,17.1%\", \"MBTI-TYPE\": \"INTJ\", \"LEVEL\": 1}, \"MBTI_INCOME_ENTITY\": {\"E_I_NET\": \"51.61%,48.39%,3.22%\", \"S_N_NET\": \"61.33%,39.14%,22.19%\", \"T_F_NET\": \"45.37%,54.63%,-9.26%\", \"J_P_NET\": \"39.5%,60.5%,-21.0%\", \"MBTI-TYPE\": \"ESFP\", \"LEVEL\": 1}, \"MBTI_AGE_EDUCATION_ENTITY\": {\"E_I_NET\": \"29.26%,70.74%,-41.48%\", \"S_N_NET\": \"33.49%,66.97%,-33.48%\", \"T_F_NET\": \"71.01%,28.99%,42.02%\", \"J_P_NET\": \"51.96%,48.04%,3.92%\", \"MBTI-TYPE\": \"INTJ\", \"LEVEL\": 2}, \"MBTI_AGE_INCOME_ENTITY\": {\"E_I_NET\": \"48.31%,51.69%,-3.38%\", \"S_N_NET\": \"52.53%,47.93%,4.6%\", \"T_F_NET\": \"51.96%,48.04%,3.92%\", \"J_P_NET\": \"32.91%,67.09%,-34.18%\", \"MBTI-TYPE\": \"ISTP\", \"LEVEL\": 2}, \"MBTI_EDUCATION_INCOME_ENTITY\": {\"E_I_NET\": \"32.56%,67.44%,-34.88%\", \"S_N_NET\": \"42.28%,58.18%,-15.9%\", \"T_F_NET\": \"64.41%,35.59%,28.82%\", \"J_P_NET\": \"58.55%,41.45%,17.1%\", \"MBTI-TYPE\": \"INTJ\", \"LEVEL\": 2}, \"MBTI_AGE_EDUCATION_INCOME_ENTITY\": {\"E_I_NET\": \"29.26%,70.74%,-41.48%\", \"S_N_NET\": \"33.49%,66.97%,-33.48%\", \"T_F_NET\": \"71.01%,28.99%,42.02%\", \"J_P_NET\": \"51.96%,48.04%,3.92%\", \"MBTI-TYPE\": \"INTJ\", \"LEVEL\": 3}}";
		logger.info("doMBTI result :" + result);
		String msg = "";
		
		if ("051501".equals(result)) {
			msg = "用户身份证号码输入有误";
		} else if ("051502".equals(result)) {
			msg = "用户手机号输入位数有误";
		} else if ("051503".equals(result)) {
			msg = "用户受教育程度标签输入有误";
		} else if ("051504".equals(result)) {
			msg = "用户收入状况标签输入有误";
		} else {
			if (StringUtils.isNotBlank(result)) {
				JSONObject all = JSONObject.fromObject(result);
				JSONObject maeieObj = JSONObject.fromObject(all.get("PERSON_MBTI_ENTITY"));
				
				String E_I_NET = maeieObj.getString("E_I_NET");//外向-内向-绝对值
				String S_N_NET = maeieObj.getString("S_N_NET");//感觉-直觉-绝对值
				String T_F_NET = maeieObj.getString("T_F_NET");//思维-情感-绝对值
				String J_P_NET = maeieObj.getString("J_P_NET");//判断-知觉-绝对值
				String MBTI_TYPE = maeieObj.getString("MBTI-TYPE");//MBTI类型
				String LEVEL = maeieObj.getString("LEVEL");//组合深度
				
				logger.info(E_I_NET);
				logger.info(S_N_NET);
				logger.info(T_F_NET);
				logger.info(J_P_NET);
				logger.info(MBTI_TYPE);
				logger.info(LEVEL);
			}
		}
		
		logger.info(msg);
		
	}
	
	/**
	 * get
	 * 
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doGet(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpGet request = new HttpGet(buildUrl(host, path, querys));
    	if (headers != null) {
    		for (Map.Entry<String, String> e : headers.entrySet()) {
    			request.addHeader(e.getKey(), e.getValue());
    		}
    	}
        
        return httpClient.execute(request);
    }
	
	/**
	 * post form
	 * 
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param bodys
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys, 
			Map<String, String> bodys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        if (headers != null) {
        	for (Map.Entry<String, String> e : headers.entrySet()) {
            	request.addHeader(e.getKey(), e.getValue());
            }
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }	
	
	/**
	 * Post String
	 * 
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys, 
			String body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
        	request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }
	
	/**
	 * Post stream
	 * 
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys, 
			byte[] body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
        	request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }
	
	/**
	 * Put String
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPut(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys, 
			String body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
        	request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }
	
	/**
	 * Put stream
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPut(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys, 
			byte[] body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
        	request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }
	
	/**
	 * Delete
	 *  
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doDelete(String host, String path, String method, 
			Map<String, String> headers, 
			Map<String, String> querys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }
        
        return httpClient.execute(request);
    }
	
	private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(host);
    	if (!StringUtils.isBlank(path)) {
    		sbUrl.append(path);
        }
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append("?").append(sbQuery);
        	}
        }
    	
    	return sbUrl.toString();
    }
	
	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = new DefaultHttpClient();
		if (host.startsWith("https://")) {
			sslClient(httpClient);
		}
		
		return httpClient;
	}
	
	private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                	
                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                	
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
        	throw new RuntimeException(ex);
        }
    }
}
