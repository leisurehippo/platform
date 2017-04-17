package com.bisaibang.monojwt.service.util;

import org.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by xiazhen on 2017/2/18.
 */
public class HttpRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);    //日志记录

    public static JSONObject httpPost(String url, List<NameValuePair> jsonParam) {
        return httpPost(url, jsonParam, false,null);
    }
    public static JSONObject httpPost(String url, List<NameValuePair> jsonParam,String Authorization) {
        return httpPost(url, jsonParam, false,Authorization);
    }

    /**
     * post请求
     *
     * @param url            url地址
     * @param jsonParam      参数
     * @param noNeedResponse 不需要返回结果
     * @param Authorization  用户名密码信息
     * @return
     */
    public static JSONObject httpPost(String url, List<NameValuePair> jsonParam, boolean noNeedResponse, String Authorization) {
        //post请求返回结果
        HttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject jsonResult = new JSONObject();
        HttpPost method = new HttpPost(url);
        if (Authorization != null) {
            method.setHeader("Authorization",Authorization);
        }
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                method.setEntity(new UrlEncodedFormEntity(jsonParam, "UTF-8"));
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    String str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    logger.debug(str);
                    /**把json字符串转换成json对象**/
                    jsonResult = new JSONObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }


    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static JSONObject httpGet(String url) {
        //get请求返回结果
        JSONObject jsonResult = new JSONObject();
        try {
            HttpClient client = HttpClientBuilder.create().build();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse result = client.execute(request);
            //logger.debug(EntityUtils.toString(result.getEntity()));

            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    String str = EntityUtils.toString(result.getEntity());
                    logger.debug(str);
                    /**把json字符串转换成json对象**/
                    jsonResult = new JSONObject(str);
                } catch (Exception e) {
                    logger.error("get请求提交失败1:" + url, e);
                }
            } else {
                logger.error("get请求提交失败2:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败3:" + url, e);
        }
        return jsonResult;
    }
}

