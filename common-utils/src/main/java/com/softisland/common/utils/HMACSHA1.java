package com.softisland.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by liwx on 16/3/15.
 */
public class HMACSHA1 {

    private static byte[] steamGuardCodeTranslations = new byte[] { 50, 51, 52, 53, 54, 55, 56, 57, 66, 67, 68, 70, 71, 72, 74, 75, 77, 78, 80, 81, 82, 84, 86, 87, 88, 89 };

    private static final String MAC_NAME = "HmacSHA1";

    private static final String ENCODING = "UTF-8";


    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] hmacSHA1Encrypt(byte[] encryptText, String encryptKey)throws Exception{
        byte[] data = new org.apache.commons.codec.binary.Base64().decode(encryptKey);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        //完成 Mac 操作
        return mac.doFinal(encryptText);
    }

    /**
     * 将二进制数据转换为base64字符串格式
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String toBase64String(byte[] bytes)throws Exception{
        byte[] data = new org.apache.commons.codec.binary.Base64().decode(bytes);
        return new String(data,"UTF-8");
    }

    /**
     * 64位编码
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encodeBase64String(byte[] bytes)throws Exception{
        return new String(new Base64().encode(bytes),"UTF-8");
    }


    public static void main(String[] args)throws Exception {
        String sharedSecret = "8JMHcHRwEPPHYs6vjyUAMfWWOdY=";
        String url = "https://api.steampowered.com/ITwoFactorService/QueryTime/v1/";

    }
}
