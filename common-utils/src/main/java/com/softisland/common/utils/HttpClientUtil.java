package com.softisland.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
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

import com.softisland.common.utils.bean.SoftCookie;
import com.softisland.common.utils.bean.SoftHeader;
import com.softisland.common.utils.bean.SoftHttpResponse;

/**
 * Created by liwx on 2015/10/28.
 */
public class HttpClientUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";
    /**
	 * 默认：请求获取数据的超时时间，单位毫秒。
	 */
	private static final int defaultSocketTimeout = 60000;
	/**
	 * 默认：设置连接超时时间，单位毫秒。
	 */
	private static final int defaultConnectTimeout = 60000;
	
	private static final int LEN_HOST_PORT = 2;

    private static final String SOFT_USER_AGENT = "Softisland SOA-Agent/1.0";
	
    /**
     * 通过GET方式获取指定URL的内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SoftHttpResponse getUrlContent(String url, SoftCookie[] softCookies)throws Exception{
        CloseableHttpClient httpclient = getHttpClient(softCookies);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        return getResponseContent(response);
    }

    public static SoftHttpResponse getUrlContent(String url)throws Exception{

        return getUrlContent(url,null);
    }

    /**
     * 获取header和返回内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SoftHttpResponse getHeadersAndContent(String url,SoftCookie[] softCookies)throws Exception{
        CloseableHttpClient httpclient = getHttpClient(softCookies);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout).setRedirectsEnabled(false)
                .build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseHeaderAndContent(response);

    }
    
    /**
     * 获取header和返回内容
     * @param url
     * @return
     * @throws Exception
     */
    public static SoftHttpResponse getHeadersAndContent1(String url,CloseableHttpClient httpclient)throws Exception{
//        CloseableHttpClient httpclient = getHttpClient(softCookies);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout).setRedirectsEnabled(false)
                .build();
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
    private static SoftHttpResponse getResponseHeaderAndContent(CloseableHttpResponse response) throws IOException {
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
    public static SoftHttpResponse getResponseContent(CloseableHttpResponse response)throws IOException{
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
    public static SoftHttpResponse postParamsToUrl(String url,Map<String,String> paraMap,SoftCookie[] cookies)throws Exception{
        return postParamsToUrl(url,paraMap,cookies,null);
    }

    public static SoftHttpResponse postParamsToUrl(String url,Map<String,String> paraMap,SoftCookie[] cookies,SoftHeader[] headers)throws IOException{
        CloseableHttpClient httpclient = getHttpClient(cookies);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout)
                .build();
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
    public static SoftHttpResponse postParamsToUrl1(String url,Map<String,String> paraMap,SoftCookie[] cookies)throws Exception{
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
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout)
                .build();
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpPost);

        return getResponseHeaderAndContent(response);
    }

    /**
     * 获取httclient
     * @param cookies
     * @return
     */
    private static CloseableHttpClient getHttpClient(SoftCookie[] cookies){
        CloseableHttpClient httpclient;
        if(null == cookies || cookies.length == 0){
            httpclient = HttpClients.createDefault();
        }else{
            CookieStore cookieStore = new BasicCookieStore();
            for(SoftCookie softCookie : cookies){
                BasicClientCookie cookie = new BasicClientCookie(softCookie.getName(),softCookie.getValue());
                cookie.setDomain(softCookie.getDomain());
                cookie.setPath(StringUtils.isBlank(softCookie.getPath())?"/":softCookie.getPath());
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
    public static SoftHttpResponse postJsonDataToUrl(String url,String json)throws Exception{
        return postJsonDataToUrl(url,json,null);
    }

    public static SoftHttpResponse postJsonDataToUrl(String url,String json,SoftCookie[] softCookies)throws Exception{

        return postJsonDataToUrl(url,json,softCookies,null);
    }

    public static SoftHttpResponse postJsonDataToUrl(String url, String json, SoftCookie[] softCookies, SoftHeader[] headers)throws Exception{

        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        if(null != headers){
            for(SoftHeader header : headers){
                httpPost.setHeader(header.getName(),header.getValue());
            }
        }
        if(StringUtils.isNotBlank(json)){
        	StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json; charset=utf-8");
            httpPost.setEntity(entity);
        }
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpPost);

        return getResponseContent(response);
    }


    /**
     * get HttpURLConnection
     * @param strUrl url��ַ
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }

    /**
     * get HttpsURLConnection
     * @param strUrl url��ַ
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                .openConnection();
        return httpsURLConnection;
    }

    public static String getURL(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }
            return strUrl;
        }
        return strUrl;

    }

    public static String getQueryString(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(indexOf+1, strUrl.length());
            }
            return "";
        }
        return strUrl;
    }

    public static Map queryString2Map(String queryString) {
        if(null == queryString || "".equals(queryString)) {
            return null;
        }
        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for(int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }
        return m;

    }

    public static void putMapByPair(String pair, Map m) {

        if(null == pair || "".equals(pair)) {
            return;
        }
        int indexOf = pair.indexOf("=");
        if(-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf+1, pair.length());
            if(null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while( (line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 用get方法调用一个url
     * 返回json字符串
     */
    public static SoftHttpResponse getJson(String url)throws IOException{
        return getJson(url,null);
    }

    /**
     * 通过代理获取steam库存
     * @param steamId
     * @return
     * @throws IOException
     */
    public static SoftHttpResponse getSteamStock(String steamId)throws IOException{
        SoftCookie cookie = new SoftCookie();
        cookie.setDomain("steamcommunity.com");
        cookie.setName("Steam_Language");
        cookie.setValue("schinese");
        cookie.setPath("/");
        CloseableHttpClient httpclient = getHttpClient(new SoftCookie[]{cookie});
        String url = "http://steamcommunity.com/profiles/"+steamId+"/inventory/json/730/2/";
        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
        //CN|782be65066aff833853feb3d472d9f55
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout)
                .build();

        httpGet.setConfig(requestConfig);

        return getResponseContent(httpclient.execute(httpGet));
    }

    /**
     * 使用GET方法获取指定URL的内容
     * @param url
     * @param softCookies
     * @return
     * @throws IOException
     */
    public static SoftHttpResponse getJson(String url,SoftCookie[] softCookies)throws IOException{
        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent",SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout)
                .build();

        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseContent(response);
    }
    
    /**
     * 使用GET方法获取指定URL的内容
     * @param url
     * @param softCookies
     * @param proxyHost 代理主机
     * @return
     * @throws IOException
     */
    public static SoftHttpResponse getJson(String url,SoftCookie[] softCookies, String proxyHost)throws IOException{
        CloseableHttpClient httpclient = getHttpClient(softCookies);

        HttpGet httpGet = new HttpGet(new String(url.getBytes(), "UTF-8"));
        httpGet.setHeader("User-Agent",SOFT_USER_AGENT);
        
        org.apache.http.client.config.RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig
                .custom().setSocketTimeout(defaultSocketTimeout).setConnectTimeout(defaultConnectTimeout);
        HttpHost host = getProxy(proxyHost);
        if (null != host)
        {
            builder.setProxy(host);
        }

        org.apache.http.client.config.RequestConfig requestConfig = builder.build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpGet);
        return getResponseContent(response);
    }
    
    private static HttpHost getProxy(String proxyHost)
    {
        String[] hostPort = proxyHost.split(":");
        if (hostPort.length < LEN_HOST_PORT)
        {
            return null;
        }
        
        HttpHost proxy = new HttpHost(hostPort[0], Integer.parseInt(hostPort[1]));
        return proxy;
    }
}
