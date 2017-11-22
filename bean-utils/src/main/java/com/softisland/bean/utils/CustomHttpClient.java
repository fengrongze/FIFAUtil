package com.softisland.bean.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.common.utils.bean.SoftHttpResponse;

import javax.net.ssl.SSLContext;

public class CustomHttpClient {


	private static final Logger log = LoggerFactory.getLogger(CustomHttpClient.class);

	 /**
	 * 默认：请求获取数据的超时时间，单位毫秒。
	 */
	private static final int defaultSocketTimeout = 60000;
	/**
	 * 默认：设置连接超时时间，单位毫秒。
	 */
	private static final int defaultConnectTimeout = 60000;

    private static final String SOFT_USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0.1; Redmi Note 3 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Mobile Crosswalk/13.42.319.12 Mobile Safari/537.36";


    public static CloseableHttpClient create(){
    	CookieStore cookieStore = new BasicCookieStore();
    	CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    	return httpClient;
    }


    /**
     * 初始化Builder
     * @param IPP
     * @return
     */
    public static RequestConfig.Builder initBuilder(String ...IPP){
    	 RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
                 .setSocketTimeout(defaultSocketTimeout)
                 .setConnectTimeout(defaultConnectTimeout);
    	 //如果代理IP不等于空
    	 if(IPP!=null && IPP.length>1 && IPP[0].indexOf("127.0.0.1")==-1){
    		 HttpHost proxy = new HttpHost(IPP[0],Integer.valueOf(IPP[1]));
    		 builder.setProxy(proxy);
    	 }
    	return builder;
    }


    public static SoftHttpResponse get(String url,String... IPP)throws Exception{
    	return get(url, null, null,IPP);
    }

	/**
     * GET
     * @param url
     * @return
     * @throws Exception
     */
    public static SoftHttpResponse get(String url,Map<String, String> headers,CookieStore cookies,String ...IPP)throws Exception{
    	CloseableHttpClient httpclient = getHttpClient(cookies);
    	HttpClientContext context = HttpClientContext.create();
        HttpGet httpGet = new HttpGet(url);
        if(null != headers){
            for(String key : headers.keySet()){
            	httpGet.setHeader(key,headers.get(key));
            }
        }
        httpGet.setHeader("User-Agent", SOFT_USER_AGENT);
//        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
//                .setSocketTimeout(defaultSocketTimeout)
//                .setConnectTimeout(defaultConnectTimeout).setRedirectsEnabled(false)
//                .build();

        org.apache.http.client.config.RequestConfig requestConfig = initBuilder(IPP).setRedirectsEnabled(false)
              .build();
        httpGet.setConfig(requestConfig);


        CloseableHttpResponse response = httpclient.execute(httpGet,context);

        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;

    }

    public static HttpClientConnectionManager init(){
        try {
            SSLContext sslContext  = SSLContexts.custom().loadTrustMaterial(null, new org.apache.http.conn.ssl.TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslContext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry registry = RegistryBuilder
                    . create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslsf).build();
            return new PoolingHttpClientConnectionManager(registry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建一个SSL信任所有证书的httpClient对象
     *
     * @return
     */
    public static CloseableHttpClient createSSLInsecureClient(CookieStore cookies) {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 默认信任所有证书
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            // AllowAllHostnameVerifier: 这种方式不对主机名进行验证，验证功能被关闭，是个空操作(域名验证)
            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return HttpClients.custom().setDefaultCookieStore(cookies).setSSLSocketFactory(sslcsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }



    /**
     * 获取httclient
     * @param cookies
     * @return
     */
    private static CloseableHttpClient getHttpClient(CookieStore cookies){
        CloseableHttpClient httpclient;
        if(null == cookies||cookies.getCookies().size()==0){
            httpclient = HttpClients.createDefault();
        }else{
            CookieStore cookieStore = new BasicCookieStore();
            List<Cookie> cookiesList = cookies.getCookies();
            for(Cookie softCookie : cookiesList){
                BasicClientCookie cookie = new BasicClientCookie(softCookie.getName(),softCookie.getValue());
                cookie.setDomain(softCookie.getDomain());
                cookie.setPath("/");
                cookieStore.addCookie(cookie);
            }
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        }
        return httpclient;
    }

    public static SoftHttpResponse post(String url,Map<String,String> paraMap,CookieStore cookies,String ...IPP)throws IOException{
    	return post(url, paraMap, cookies, null,IPP);
    }

    /**
     *
     *
     * POST
     * @param url
     * @param paraMap
     * @return
     * @throws IOException
     */
    public static SoftHttpResponse post(String url,Map<String,String> paraMap,CookieStore cookies,Map<String, String> headers,String ...IPP)throws IOException{
    	CloseableHttpClient httpclient = getHttpClient(cookies);
    	HttpClientContext context = HttpClientContext.create();
    	HttpPost httpPost = new HttpPost(url);
        if(null != headers){
            for(String key : headers.keySet()){
            	httpPost.setHeader(key,headers.get(key));
            }
        }
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
//        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
//                .setSocketTimeout(defaultSocketTimeout)
//                .setConnectTimeout(defaultConnectTimeout)
//                .build();

        org.apache.http.client.config.RequestConfig requestConfig = initBuilder(IPP).build();
        httpPost.setConfig(requestConfig);

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

        CloseableHttpResponse response = httpclient.execute(httpPost,context);

        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;
    }


    public static SoftHttpResponse delete(String url,CookieStore cookies,String ...IPP)throws IOException{
    	return delete(url, cookies, null,IPP);
    }

    /**
     * POST
     * @param url
     * @return
     * @throws IOException
     */
    public static SoftHttpResponse delete(String url,CookieStore cookies,Map<String, String> headers,String ...IPP)throws IOException{
    	CloseableHttpClient httpclient = getHttpClient(cookies);
    	HttpClientContext context = HttpClientContext.create();
    	HttpDelete httpDelete = new HttpDelete(url);
        if(null != headers){
            for(String key : headers.keySet()){
            	httpDelete.setHeader(key,headers.get(key));
            }
        }
        httpDelete.setHeader("User-Agent", SOFT_USER_AGENT);
//        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
//                .setSocketTimeout(defaultSocketTimeout)
//                .setConnectTimeout(defaultConnectTimeout)
//                .build();

        org.apache.http.client.config.RequestConfig requestConfig = initBuilder(IPP).build();



        httpDelete.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpDelete,context);

        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;
    }

    /**
     * 返回请求内容
     * @param response
     * @return
     * @throws Exception
     */
    public static SoftHttpResponse getResponseContent(CloseableHttpResponse response,String ...IPP)throws IOException{
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


    public static SoftHttpResponse postJsonDataToUrl(String url,String json,CookieStore cookies,Map<String, String> headers,String ...IPP)throws Exception{
    	CloseableHttpClient httpclient = getHttpClient(cookies);
    	HttpClientContext context = HttpClientContext.create();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", SOFT_USER_AGENT);
        if(null != headers){
            for(String key : headers.keySet()){
            	httpPost.setHeader(key,headers.get(key));
            }
        }

//        if(StringUtils.isNotBlank(json)){
            StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
//            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
//        }


//        RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom()
//                .setSocketTimeout(defaultSocketTimeout)
//                .setConnectTimeout(defaultConnectTimeout);

        org.apache.http.client.config.RequestConfig requestConfig = initBuilder(IPP).build();
//        org.apache.http.client.config.RequestConfig requestConfig = builder.build();

        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpPost,context);

        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;
    }





    public static SoftHttpResponse putJsonDataToUrl(String url, String json, CookieStore cookies, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = getHttpClient(cookies);
        HttpClientContext context = HttpClientContext.create();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("User-Agent", SOFT_USER_AGENT);
        if(null != headers) {
            Iterator entity = headers.keySet().iterator();

            while(entity.hasNext()) {
                String builder = (String)entity.next();
                httpPut.setHeader(builder, (String)headers.get(builder));
            }
        }

        StringEntity entity1 = new StringEntity(json, "utf-8");
        entity1.setContentType("application/json");
        httpPut.setEntity(entity1);
        RequestConfig.Builder builder1 = RequestConfig.custom().setSocketTimeout('\uea60').setConnectTimeout('\uea60');
        RequestConfig requestConfig = builder1.build();
        httpPut.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpPut, context);
        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = CustomHttpClient.getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;
    }


    public static SoftHttpResponse deleteDataToUrl(String url, CookieStore cookies, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = getHttpClient(cookies);
        HttpClientContext context = HttpClientContext.create();
       HttpDelete httpDelete = new HttpDelete(url);
        if(null != headers){
            for(String key : headers.keySet()){
                httpDelete.setHeader(key,headers.get(key));
            }
        }
        httpDelete.setHeader("User-Agent", SOFT_USER_AGENT);
        org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setSocketTimeout(defaultSocketTimeout)
                .setConnectTimeout(defaultConnectTimeout).setRedirectsEnabled(false)
                .build();
        httpDelete.setConfig(requestConfig);


        CloseableHttpResponse response = httpclient.execute(httpDelete,context);

        CookieStore cookieStore = context.getCookieStore();
        SoftHttpResponse softHttpResponse = getResponseContent(response);
        softHttpResponse.setCookieStore(cookieStore);
        return softHttpResponse;
    }

}
