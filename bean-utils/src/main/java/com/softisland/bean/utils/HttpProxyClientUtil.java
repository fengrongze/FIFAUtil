package com.softisland.bean.utils;

import com.softisland.common.utils.bean.SoftCookie;
import com.softisland.common.utils.bean.SoftHeader;
import com.softisland.common.utils.bean.SoftHttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liwx on 2015/10/28.
 */
//@Component
public class HttpProxyClientUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpProxyClientUtil.class);

    /**
	 * 默认：请求获取数据的超时时间，单位毫秒。
	 */
	private static final int defaultSocketTimeout = 20000;
	/**
	 * 默认：设置连接超时时间，单位毫秒。
	 */
	private static final int defaultConnectTimeout = 3000;

    private static final String SOFT_USER_AGENT = "Softisland SOA-Agent/1.0";

    @Value("${proxy.host:127.0.0.1}")
    private String proxyHost;

    @Value("${proxy.port:3128}")
    private int proxyPort;
	
    /**
     * 通过GET方式获取指定URL的内容
     * @param url
     * @return
     * @throws Exception
     */
    public SoftHttpResponse getUrlContent(String url, SoftCookie[] softCookies)throws Exception{
        CloseableHttpClient httpclient = getHttpClient(softCookies);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();

        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        return getResponseContent(response);
    }

    public SoftHttpResponse getUrlContent(String url)throws Exception{

        return getUrlContent(url,null);
    }

    /**
     * 获取header和返回内容
     * @param url
     * @return
     * @throws Exception
     */
    public SoftHttpResponse getHeadersAndContent(String url,SoftCookie[] softCookies)throws Exception{
        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();

        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseHeaderAndContent(response);

    }

    /**
     * 获取header和返回内容
     * @param response
     * @return
     * @throws IOException
     */
    private SoftHttpResponse getResponseHeaderAndContent(CloseableHttpResponse response) throws IOException {
        SoftHttpResponse res = getResponseContent(response);
        Header[] headers = response.getAllHeaders();
        if(null != headers){
            Map<String,String> map = new HashMap<>();
            for(Header h : headers){
                if(map.containsKey(h.getName())){
                    map.put(h.getName(),map.get(h.getName())+";"+h.getValue());
                }else{
                    map.put(h.getName(),h.getValue());
                }
            }
            res.setHeaders(map);
        }
        return res;
    }

    /**
     * 返回请求内容
     * @param response
     * @return
     * @throws Exception
     */
    public SoftHttpResponse getResponseContent(CloseableHttpResponse response)throws IOException{
        try {
        	SoftHttpResponse res = new SoftHttpResponse(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(),"utf-8"));
            Header[] headers = response.getAllHeaders();
            if(null != headers){
                Map<String,String> map = new HashMap<>();
                for(Header h : headers){
                    if(map.containsKey(h.getName())){
                        map.put(h.getName(),map.get(h.getName())+";"+h.getValue());
                    }else{
                        map.put(h.getName(),h.getValue());
                    }
                }
                res.setHeaders(map);
            }
               return res;
        }finally {
            response.close();
        }
    }



    /**
     * 向指定的URL发送请求
     * @param url
     * @param paraMap 参数
     * @return
     * @throws Exception
     */
    public SoftHttpResponse postParamsToUrl(String url,Map<String,String> paraMap,SoftCookie[] cookies)throws Exception{
        return postParamsToUrl(url,paraMap,cookies,null);
    }

    public SoftHttpResponse postParamsToUrl(String url,Map<String,String> paraMap,SoftCookie[] cookies,SoftHeader[] headers)throws IOException{
        CloseableHttpClient httpclient = getHttpClient(cookies);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();


        httpPost.setConfig(requestConfig);

        if(null != headers){
            for(SoftHeader header : headers){
                httpPost.setHeader(header.getName(),header.getValue());
            }
        }
        try {
            if(null != paraMap && !paraMap.isEmpty()){
                List<NameValuePair> nvps = new ArrayList<>(paraMap.size());
                paraMap.keySet().forEach(v->nvps.add(new BasicNameValuePair(v, paraMap.get(v))));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            log.error("<不支持的转码>",e);
            e.printStackTrace();
        }

        CloseableHttpResponse response = httpclient.execute(httpPost);

        return getResponseContent(response);
    }

    /**
     * 返回header和内容
     * @param url
     * @param paraMap
     * @return
     * @throws Exception
     */
    public SoftHttpResponse postParamsToUrl1(String url,Map<String,String> paraMap,SoftCookie[] cookies)throws Exception{
        CloseableHttpClient httpclient = getHttpClient(cookies);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        List<NameValuePair> nvps = new ArrayList<>(paraMap.size());
        paraMap.keySet().forEach(v->nvps.add(new BasicNameValuePair(v, paraMap.get(v))));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("<不支持的转码>",e);
            e.printStackTrace();
        }

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();

        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpPost);

        return getResponseHeaderAndContent(response);
    }

    /**
     * 获取httclient
     * @param cookies
     * @return
     */
    private CloseableHttpClient getHttpClient(SoftCookie[] cookies){
        CloseableHttpClient httpclient;
        if(null == cookies || cookies.length == 0){
            httpclient = HttpClients.createDefault();
        }else{
            CookieStore cookieStore = new BasicCookieStore();
            for(SoftCookie softCookie : cookies){
                BasicClientCookie cookie = new BasicClientCookie(softCookie.getName(),softCookie.getValue());
                cookie.setDomain(softCookie.getDomain());
                cookie.setPath("/");
                cookieStore.addCookie(cookie);
            }
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        }
        return httpclient;
    }



    /**
     * 发送json数据到指定的URL
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public SoftHttpResponse postJsonDataToUrl(String url,String json)throws Exception{
        return postJsonDataToUrl(url,json,null);
    }

    public SoftHttpResponse postJsonDataToUrl(String url,String json,SoftCookie[] softCookies)throws Exception{

        return postJsonDataToUrl(url,json,softCookies,null);
    }

    public SoftHttpResponse postJsonDataToUrl(String url, String json, SoftCookie[] softCookies, SoftHeader[] headers)throws Exception{

        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        if(null != headers){
            for(SoftHeader header : headers){
                httpPost.setHeader(header.getName(),header.getValue());
            }
        }

        StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json; charset=utf-8");
        httpPost.setEntity(entity);

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();

        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpPost);

        return getResponseContent(response);
    }

    /**
     * 用get方法调用一个url
     * 返回json字符串
     */
    public SoftHttpResponse getJson(String url)throws IOException{
        return getJson(url,null);
    }


    private HttpHost getProxy(){
        int i = new Random().nextInt(10);
        //HttpHost proxy = new HttpHost(hosts[i].split(":")[0],Integer.parseInt(hosts[i].split(":")[1]));
        if("127.0.0.1".equals(proxyHost)){
            return null;
        }
        HttpHost proxy = new HttpHost(proxyHost,proxyPort);
        return proxy;
    }

    /**
     * 使用GET方法获取指定URL的内容
     * @param url
     * @param softCookies
     * @return
     * @throws IOException
     */
    public SoftHttpResponse getJson(String url,SoftCookie[] softCookies)throws IOException{
        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent","Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");

        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout);
        if(null != getProxy()){
            builder.setProxy(getProxy());
        }
        org.apache.http.client.config.RequestConfig requestConfig = builder.build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseContent(response);
    }
    
    
}
