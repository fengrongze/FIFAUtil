package com.softisland.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by liwx on 2015/4/6.
 */
public class SHAEncoder {
    /**
     * SHA-1消息摘要算法,返回字节数组
     */
    private static byte[] encodeSHA(byte[] data) throws Exception {
        return DigestUtils.sha1(data);
    }

    /**
     * SHA-1消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHAHex(byte[] data) throws Exception {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * SHA-256消息摘要算法,返回字节数组
     */
    private static byte[] encodeSHA256(byte[] data) throws Exception {
        return DigestUtils.sha256(data);
    }

    /**
     * SHA-256消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHA256Hex(byte[] data) throws Exception {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * SHA-256消息摘要算法,返回十六进制字符串
     */
    public static String encodeSHA256Hex(String data) throws Exception {
        return DigestUtils.sha256Hex(DigestUtils.md5(data));
    }

    /**
     * SHA-384消息摘要算法,返回字节数组
     */
    private static byte[] encodeSHA384(byte[] data) throws Exception {
        return DigestUtils.sha384(data);
    }

    /**
     * SHA-384消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHA384Hex(byte[] data) throws Exception {
        return DigestUtils.sha384Hex(data);
    }

    /**
     * SHA-384消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHA384Hex(String data) throws Exception {
        return DigestUtils.sha384Hex(data);
    }

    /**
     * SHA-512消息摘要算法,返回字节数组
     */
    private static byte[] encodeSHA512(byte[] data) throws Exception {
        return DigestUtils.sha512(data);
    }

    /**
     * SHA-512消息摘要算法,返回字节数组
     */
    private static byte[] encodeSHA512(String data) throws Exception {
        return DigestUtils.sha512(data);
    }

    /**
     * SHA-512消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHA512Hex(byte[] data) throws Exception {
        return DigestUtils.sha512Hex(data);
    }
    /**
     * SHA-512消息摘要算法,返回十六进制字符串
     */
    private static String encodeSHA512Hex(String data) throws Exception {

        return DigestUtils.sha512Hex(DigestUtils.md5(data));
    }

    public static void main(String[] args) throws Exception{
        System.out.println(System.currentTimeMillis());
        System.out.println(encodeSHA256Hex("123456"));
        System.out.println(System.currentTimeMillis());
    }
}
