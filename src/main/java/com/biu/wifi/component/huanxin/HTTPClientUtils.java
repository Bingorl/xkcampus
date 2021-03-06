package com.biu.wifi.component.huanxin;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * HTTPClient 工具类
 *
 * @author HJD
 */
public class HTTPClientUtils {
    /**
     * Send SSL Request
     *
     * @param reqURL
     * @param token
     * @param str
     * @return
     */
    public static ObjectNode sendHTTPRequest(URL url, Credentail credentail, Object dataBody,
                                             String method) {

        HttpClient httpClient = getClient(true);

        ObjectNode resObjectNode = factory.objectNode();

        try {

            HttpResponse response = null;

            if (method.equals("POST")) {
                HttpPost httpPost = new HttpPost(url.toURI());

                if (credentail != null) {
                    // Token.applyAuthentication(httpPost, credentail);
                }
                httpPost.setEntity(new StringEntity(dataBody.toString(), "UTF-8"));

                response = httpClient.execute(httpPost);
            } else if (method.equals("PUT")) {
                HttpPut httpPut = new HttpPut(url.toURI());
                if (credentail != null) {
                    // Token.applyAuthentication(httpPut, credentail);
                }
                httpPut.setEntity(new StringEntity(dataBody.toString(), "UTF-8"));

                response = httpClient.execute(httpPut);
            } else if (method.equals("GET")) {

                HttpGet httpGet = new HttpGet(url.toURI());
                if (credentail != null) {
                    // Token.applyAuthentication(httpGet, credentail);
                }

                response = httpClient.execute(httpGet);
            } else if (method.equals("DELETE")) {
                HttpDelete httpDelete = new HttpDelete(url.toURI());

                if (credentail != null) {
                    // Token.applyAuthentication(httpDelete, credentail);
                }

                response = httpClient.execute(httpDelete);
            }

            HttpEntity entity = response.getEntity();

            if (null != entity) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(responseContent);

                resObjectNode = mapper.readTree(jp);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return resObjectNode;
    }

    /**
     * Create a httpClient instance
     *
     * @param isSSL
     * @return HttpClient instance
     */
    @SuppressWarnings("unused")
    public static HttpClient getClient(boolean isSSL) {

        HttpClient httpClient = new DefaultHttpClient();
        if (isSSL) {
            X509TrustManager xtm = new X509TrustManager() {

                public void checkClientTrusted1(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted1(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };

            try {
                SSLContext ctx = SSLContext.getInstance("TLS");

                ctx.init(null, new TrustManager[]{xtm}, null);

                SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

                httpClient.getConnectionManager().getSchemeRegistry()
                        .register(new Scheme("https", 443, socketFactory));

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        return httpClient;
    }

    private static JsonNodeFactory factory = new JsonNodeFactory(false);

    public static URL getURL(String path) {
        URL url = null;

        try {
            url = new URL("https", "a1.easemob.com", "/" + path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Check illegal String
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.lookingAt();
    }
}
