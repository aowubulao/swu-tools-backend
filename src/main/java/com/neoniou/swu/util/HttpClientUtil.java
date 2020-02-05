package com.neoniou.swu.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * HttpClient 封装
 *
 * @author Neo.Zzj
 */
public class HttpClientUtil {

    private PoolingHttpClientConnectionManager httpClientPool;

    private static final String UAAAP_URL = "https://uaaap.swu.edu.cn/cas/login?service=http://i.swu.edu.cn/PersonalApplications/viewPageV3";
    private static final String GRADE_URL = "http://jw.swu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005";
    private static final String COURSE_URL = "http://jw.swu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151";

    private String cookie;
    private String lt;

    /**
     * Constructor
     */
    public HttpClientUtil() {
        this.httpClientPool = new PoolingHttpClientConnectionManager();
        //配置
        this.httpClientPool.setMaxTotal(100);
        this.httpClientPool.setDefaultMaxPerRoute(10);
    }

    /**
     * 验证用户名密码
     * @return 用户名密码是否正确
     */
    public Boolean checkAccount(final String username, final String password) {

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();
        //获取 cookie 和 lt
        this.getCookieAndLt();

        HttpPost httpPost = new HttpPost(UAAAP_URL);
        httpPost.setConfig(this.getConfig());

        // Request头信息
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (" +
                "KHTML, like Gecko) Chrome/79.0.3945.130 Mobile Safari/537.36");
        httpPost.setHeader("Cookie", cookie);

        // loginForm
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("lt", lt));
        params.add(new BasicNameValuePair("execution", "e1s1"));
        params.add(new BasicNameValuePair("_eventId", "submit"));
        params.add(new BasicNameValuePair("isQrSubmit", "false"));
        params.add(new BasicNameValuePair("qrValue", ""));

        CloseableHttpResponse response = null;
        try {
            // 放入 form
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            response = httpClient.execute(httpPost);

            // 302重定向登陆成功
            if (302 == response.getStatusLine().getStatusCode()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 根据登录 cookie 获取成绩
     *
     * @param cookie
     * @param xnm 学年    eg: 2019(2019 - 2020)
     * @param xqm 学期    3:第一学期 12:第二学期 16:第三学期
     * @return 成绩
     */
    public String getGrades(final String cookie, final String xnm, final String xqm) {

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(GRADE_URL);
            //输入网址，创建 Post 请求对象
            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Cookie", cookie);

            //参数
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("xnm", xnm));
            params.add(new BasicNameValuePair("xqm", xqm));
            params.add(new BasicNameValuePair("nd", "1580526946442"));
            params.add(new BasicNameValuePair("queryModel.showCount", "2000"));
            params.add(new BasicNameValuePair("queryModel.currentPage", "1"));
            params.add(new BasicNameValuePair("queryModel.sortName", ""));
            params.add(new BasicNameValuePair("ueryModel.sortOrder", "asc"));
            params.add(new BasicNameValuePair("time", "0"));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            //发起请求
            response = httpClient.execute(httpPost);

            //解析响应
            if (200 == response.getStatusLine().getStatusCode()) {
                HttpEntity httpEntity = response.getEntity();

                return EntityUtils.toString(httpEntity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public String getCourses(final String cookie, final String xnm, final String xqm) {

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(COURSE_URL);
            //输入网址，创建 Post 请求对象
            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Cookie", cookie);

            //参数
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("xnm", xnm));
            params.add(new BasicNameValuePair("xqm", xqm));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            //发起请求
            response = httpClient.execute(httpPost);

            //解析响应
            if (200 == response.getStatusLine().getStatusCode()) {
                HttpEntity httpEntity = response.getEntity();

                return EntityUtils.toString(httpEntity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取 Cookie 和 Lt
     */
    private void getCookieAndLt() {

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();

        HttpGet httpGet = new HttpGet(UAAAP_URL);
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            //响应结果
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

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 配置
     * @return config
     */
    private RequestConfig getConfig() {
        return RequestConfig.custom()
                //创建连接的最长时间
                .setConnectTimeout(1000)
                //获取连接的最长时间
                .setConnectionRequestTimeout(1000)
                //数据传输的最长时间
                .setSocketTimeout(10000)
                .build();
    }
}
