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

    private static final String GRADE_URL = "http://jw.swu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005";
    private static final String COURSE_URL = "http://jw.swu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151";
    private static final String UTILITY_URL = "http://211.83.23.198/account.do?command=login";

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

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Cookie", cookie);

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

            response = httpClient.execute(httpPost);

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
     * 获取课程信息
     * @param cookie
     * @param xnm
     * @param xqm
     * @return
     */
    public String getCourses(final String cookie, final String xnm, final String xqm) {

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(COURSE_URL);

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            httpPost.setHeader("Cookie", cookie);

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("xnm", xnm));
            params.add(new BasicNameValuePair("xqm", xqm));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            response = httpClient.execute(httpPost);

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
     * 查询水电费
     * @param buildId 寝室 id
     * @param roomCode 寝室门牌号
     * @return 水电费余额
     */
    public String getUtility(final String buildId, final String roomCode) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.httpClientPool).build();

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(UTILITY_URL);

            HttpPost httpPost = new HttpPost(uriBuilder.build());

            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("buildid", buildId));
            params.add(new BasicNameValuePair("roomcode", roomCode));
            params.add(new BasicNameValuePair("ckcode", "2e5g"));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
            httpPost.setEntity(formEntity);

            response = httpClient.execute(httpPost);

            if (200 == response.getStatusLine().getStatusCode()) {
                HttpEntity httpEntity = response.getEntity();

                Document dom = Jsoup.parse(EntityUtils.toString(httpEntity));
                Elements bgc = dom.getElementsByClass("bgc_white");

                int index = 1;
                for (Element e : bgc) {
                    if (index == 13) {
                        return e.text();
                    }
                    index++;
                }
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
