package com.neoniou.swu.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Neo.Zzj
 */
public class LoginUtil {

    private static final String PORTAL_URL = "https://uaaap.swu.edu.cn/cas/login?service=http://i.swu.edu.cn/PersonalApplications/viewPageV3";
    private static final String JW_URL = "https://uaaap.swu.edu.cn/cas/login?service=http://jw.swu.edu.cn/sso/zllogin";
    private static final String JW_INDEX = "http://jw.swu.edu.cn/jwglxt/xtgl/login_slogin.htm";

    String username;
    String password;

    public LoginUtil(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 登录教务系统，获取 Cookie
     * @return cookie
     */
    public String login() {

        String cookie;
        String lt = null;

        //创建 Cookie store
        BasicCookieStore cookieStore = new BasicCookieStore();

        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(true).build();

        //创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(config)
                .setDefaultCookieStore(cookieStore)
                .build();

        CloseableHttpResponse response = null;

        try {

            httpClient.execute(new HttpGet(JW_INDEX));

            URIBuilder uriBuilder = new URIBuilder(PORTAL_URL);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            response = httpClient.execute(httpGet);

            if (200 == response.getStatusLine().getStatusCode()) {

                //获取 lt
                HttpEntity httpEntity = response.getEntity();
                String content = EntityUtils.toString(httpEntity, "utf-8");
                Document dom = Jsoup.parse(content);

                Elements elements = dom.getElementsByTag("input");
                for (Element element : elements) {
                    if (element.toString().contains("lt")) {
                        lt = element.toString().substring(38, 111);
                    }
                }
                //获取 cookie
                cookie = response.getFirstHeader("Set-Cookie").getValue();
                cookie = cookie.substring(0, cookie.indexOf(";"));

            } else {
                return "NETWORK_ERROR";
            }

            //登录
            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Cookie", cookie);

            //登录参数
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("lt", lt));
            params.add(new BasicNameValuePair("execution", "e1s1"));
            params.add(new BasicNameValuePair("_eventId", "submit"));
            params.add(new BasicNameValuePair("isQrSubmit", "false"));
            params.add(new BasicNameValuePair("qrValue", ""));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            CloseableHttpResponse loginResponse = httpClient.execute(httpPost);

            //登陆失败
            if (302 != loginResponse.getStatusLine().getStatusCode()) {
                return "LOGIN_FAIL";
            }

            String tem = loginResponse.toString();
            tem = tem.substring(tem.indexOf("Location") + 1, tem.indexOf("]"));
            String location = tem.substring(9);

            httpClient.execute(new HttpGet(location));

            List<Cookie> cookies = cookieStore.getCookies();

            String CASTGC = null;
            String JSESSIONID = null;
            String JW1 = null;
            String JwJSESSIONID = null;
            for (Cookie c : cookies) {
                if ("CASTGC".equals(c.getName())) {
                    CASTGC = "; CASTGC=\"" + c.getValue() + "\"";
                }
                if ("JSESSIONID".equals(c.getName()) && "uaaap.swu.edu.cn".equals(c.getDomain())) {
                    JSESSIONID = "JSESSIONID=" + c.getValue();
                }
                if ("1".equals(c.getName())) {
                    JW1 = "1=" + c.getValue();
                }
                if ("JSESSIONID".equals(c.getName()) && "jw.swu.edu.cn".equals(c.getDomain())) {
                    JwJSESSIONID = "JSESSIONID=" + c.getValue();
                }
            }

            //2
            HttpGet login2 = new HttpGet(JW_URL);
            login2.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
            String JWCookie = JSESSIONID + CASTGC;
            login2.setHeader("Cookie", JWCookie);
            response = httpClient.execute(login2);

            location = response.getFirstHeader("Location").getValue();

            //3
            HttpGet login3 = new HttpGet(location);
            login3.setHeader("Cookie", JW1);
            login3.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
            response = httpClient.execute(login3);

            location = response.getFirstHeader("Location").getValue();

            //4
            String cookie4 = JWCookie + "; " + JW1;
            HttpGet login4 = new HttpGet(location);
            login4.setHeader("Cookie", cookie4);
            login4.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
            response = httpClient.execute(login4);

            location = response.getFirstHeader("Location").getValue();

            //5
            HttpGet login5 = new HttpGet(location);
            login4.setHeader("Cookie", JW1);
            login5.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
            response = httpClient.execute(login5);

            return JwJSESSIONID + "; " +JW1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
