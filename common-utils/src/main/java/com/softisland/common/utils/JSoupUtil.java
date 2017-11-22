/**
 * 
 */
package com.softisland.common.utils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * URL访问类
 * @author qxf
 *
 */
public class JSoupUtil
{
    // 日志打印对象
    private static final Logger LOG = LoggerFactory.getLogger(JSoupUtil.class);
    
    /**
     * 查询指定URL的信息
     * @param url URL地址
     * @param timeout 超时时间，单位毫秒
     * @return
     */
    public static String getJson(String url, int timeout)
    {
        try
        {
            return Jsoup.connect(url).timeout(timeout).ignoreContentType(true).execute().body();
        }
        catch (IOException e)
        {
            LOG.error("query for url(" + url + ") catch exception:", e);
            return null;
        }
    }
}
