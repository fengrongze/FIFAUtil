package com.softisland.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

public class FifaMd5Util {
	
	static String hex_chr = "0123456789abcdef";
	static int[] r1Shifts = { 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22 };
	static int[] r2Shifts = { 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20 };
	static int[] r3Shifts = { 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23 };
	static int[] r4Shifts = { 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };
	
	private static String numToHex(int num)
	{
        String str = "";
        for (int j = 0; j <= 3; j++)
        {
            str += hex_chr.substring((num >> (j * 8 + 4)) & 0x0F, ((num >> (j * 8 + 4)) & 0x0F)+1) + hex_chr.substring((num >> (j * 8)) & 0x0F, ((num >> (j * 8)) & 0x0F)+1);
        }
        return str;
    }

    private static ArrayList chunkMessage(String str) throws UnsupportedEncodingException
    {

        int nblk = ((str.length() + 8) >> 6) + 1;
        ArrayList blks = new ArrayList();
        for (int i = 0; i < nblk * 16; i++)
        {
            blks.add(0);
        }
        int j = 0;
        for (j = 0; j < str.length(); j++)
        {
        	byte[] bytes = str.substring(j, j+1).getBytes("unicode");
//            byte[] bytes = System.Text.Encoding.Unicode.GetBytes(str.substring(j, 1));
            String rs = IsZero(bytes[3]) + "" + IsZero(bytes[2]);
            int rs_int = Integer.valueOf(rs);
            blks.set(j >> 2,  (int)blks.get(j >> 2) | rs_int << ((j % 4) * 8));
        }
        blks.set(j >> 2, (int)blks.get(j >> 2)| 0x80 << ((j % 4) * 8));
        blks.set(nblk * 16 - 2, str.length() * 8);
        return blks;
    }

    private static int bitwiseRotate(int x, int c)
    {
        return (x << c) | MoveByte(x, 32 - c);
    }

    private static int cmn(int q, int a, int b, int x, int s, int t)
    {
        return add(bitwiseRotate(add(add(a, q), add(x, t)), s), b);
    }

    private static int add(int x, int y)
    {
        int lsw = (x & 0xFFFF) + (y & 0xFFFF);
        int msw = (x >> 16) + (y >> 16) + (lsw >> 16);
        return (msw << 16) | (lsw & 0xFFFF);
    }

    private static int md5_h(int a, int b, int c, int d, int x, int s, int t)
    {
        return cmn(b ^ c ^ d, a, b, x, s, t);
    }

    private static int md5_i(int a, int b, int c, int d, int x, int s, int t)
    {
        return cmn(c ^ (b | (~d)), a, b, x, s, t);
    }
    private static int md5_g(int a, int b, int c, int d, int x, int s, int t)
    {
        return cmn((b & d) | (c & (~d)), a, b, x, s, t);
    }

    private static int md5_f(int a, int b, int c, int d, int x, int s, int t)
    {
        return cmn((b & c) | ((~b) & d), a, b, x, s, t);
    }

    /// <summary>
    /// 这是对外提供的加密方法
    /// </summary>
    /// <param name="str"></param>
    /// <returns></returns>
    public static String md5(String str) throws UnsupportedEncodingException
    {
        ArrayList x = chunkMessage(str);

        int a = 1732584193;
        int b = -271733879;
        int c = -1732584194;
        int d = 271733878;

        for (int i = 0; i < x.size(); i += 16)
        {
            int tempA = a;
            int tempB = b;
            int tempC = c;
            int tempD = d;

            a = md5_f(a, b, c, d, (int)x.get(i+0), r1Shifts[0], -680876936);
            d = md5_f(d, a, b, c, (int)x.get(i + 1), r1Shifts[1], -389564586);
            c = md5_f(c, d, a, b, (int)x.get(i + 2), r1Shifts[2], 606105819);
            b = md5_f(b, c, d, a, (int)x.get(i + 3), r1Shifts[3], -1044525330);
            a = md5_f(a, b, c, d, (int)x.get(i + 4), r1Shifts[4], -176418897);
            d = md5_f(d, a, b, c, (int)x.get(i + 5), r1Shifts[5], 1200080426);
            c = md5_f(c, d, a, b, (int)x.get(i + 6), r1Shifts[6], -1473231341);
            b = md5_f(b, c, d, a, (int)x.get(i + 7), r1Shifts[7], -45705983);
            a = md5_f(a, b, c, d, (int)x.get(i + 8), r1Shifts[8], 1770035416);
            d = md5_f(d, a, b, c, (int)x.get(i + 9), r1Shifts[9], -1958414417);
            c = md5_f(c, d, a, b, (int)x.get(i + 10), r1Shifts[10], -42063);
            b = md5_f(b, c, d, a, (int)x.get(i + 11), r1Shifts[11], -1990404162);
            a = md5_f(a, b, c, d, (int)x.get(i + 12), r1Shifts[12], 1804603682);
            d = md5_f(d, a, b, c, (int)x.get(i + 13), r1Shifts[13], -40341101);
            c = md5_f(c, d, a, b, (int)x.get(i + 14), r1Shifts[14], -1502002290);
            b = md5_f(b, c, d, a, (int)x.get(i + 15), r1Shifts[15], 1236535329);

            a = md5_g(a, b, c, d, (int)x.get(i + 1), r2Shifts[0], -165796510);
            d = md5_g(d, a, b, c, (int)x.get(i + 6), r2Shifts[1], -1069501632);
            c = md5_g(c, d, a, b, (int)x.get(i + 11), r2Shifts[2], 643717713);
            b = md5_g(b, c, d, a, (int)x.get(i + 0), r2Shifts[3], -373897302);
            a = md5_g(a, b, c, d, (int)x.get(i + 5), r2Shifts[4], -701558691);
            d = md5_g(d, a, b, c, (int)x.get(i + 10), r2Shifts[5], 38016083);
            c = md5_g(c, d, a, b, (int)x.get(i + 15), r2Shifts[6], -660478335);
            b = md5_g(b, c, d, a, (int)x.get(i + 4), r2Shifts[7], -405537848);
            a = md5_g(a, b, c, d, (int)x.get(i + 9), r2Shifts[8], 568446438);
            d = md5_g(d, a, b, c, (int)x.get(i + 14), r2Shifts[9], -1019803690);
            c = md5_g(c, d, a, b, (int)x.get(i + 3), r2Shifts[10], -187363961);
            b = md5_g(b, c, d, a, (int)x.get(i + 8), r2Shifts[11], 1163531501);
            a = md5_g(a, b, c, d, (int)x.get(i + 13), r2Shifts[12], -1444681467);
            d = md5_g(d, a, b, c, (int)x.get(i + 2), r2Shifts[13], -51403784);
            c = md5_g(c, d, a, b, (int)x.get(i + 7), r2Shifts[14], 1735328473);
            b = md5_g(b, c, d, a, (int)x.get(i + 12), r2Shifts[15], -1926607734);

            a = md5_h(a, b, c, d, (int)x.get(i + 5), r3Shifts[0], -378558);
            d = md5_h(d, a, b, c, (int)x.get(i + 8), r3Shifts[1], -2022574463);
            c = md5_h(c, d, a, b, (int)x.get(i + 11), r2Shifts[2], 1839030562);
            b = md5_h(b, c, d, a, (int)x.get(i + 14), r3Shifts[3], -35309556);
            a = md5_h(a, b, c, d, (int)x.get(i + 1), r3Shifts[4], -1530992060);
            d = md5_h(d, a, b, c, (int)x.get(i + 4), r3Shifts[5], 1272893353);
            c = md5_h(c, d, a, b, (int)x.get(i + 7), r3Shifts[6], -155497632);
            b = md5_h(b, c, d, a, (int)x.get(i + 10), r3Shifts[7], -1094730640);
            a = md5_h(a, b, c, d, (int)x.get(i + 13), r3Shifts[8], 681279174);
            d = md5_h(d, a, b, c, (int)x.get(i + 0), r3Shifts[9], -358537222);
            c = md5_h(c, d, a, b, (int)x.get(i + 3), r3Shifts[10], -722521979);
            b = md5_h(b, c, d, a, (int)x.get(i + 6), r3Shifts[11], 76029189);
            a = md5_h(a, b, c, d, (int)x.get(i + 9), r3Shifts[12], -640364487);
            d = md5_h(d, a, b, c, (int)x.get(i + 12), r3Shifts[13], -421815835);
            c = md5_h(c, d, a, b, (int)x.get(i + 15), r3Shifts[14], 530742520);
            b = md5_h(b, c, d, a, (int)x.get(i + 2), r3Shifts[15], -995338651);

            a = md5_i(a, b, c, d, (int)x.get(i + 0), r4Shifts[0], -198630844);
            d = md5_i(d, a, b, c, (int)x.get(i + 7), r4Shifts[1], 1126891415);
            c = md5_i(c, d, a, b, (int)x.get(i + 14), r4Shifts[2], -1416354905);
            b = md5_i(b, c, d, a, (int)x.get(i + 5), r4Shifts[3], -57434055);
            a = md5_i(a, b, c, d, (int)x.get(i + 12), r4Shifts[4], 1700485571);
            d = md5_i(d, a, b, c, (int)x.get(i + 3), r4Shifts[5], -1894986606);
            c = md5_i(c, d, a, b, (int)x.get(i + 10), r4Shifts[6], -1051523);
            b = md5_i(b, c, d, a, (int)x.get(i + 1), r4Shifts[7], -2054922799);
            a = md5_i(a, b, c, d, (int)x.get(i + 8), r4Shifts[8], 1873313359);
            d = md5_i(d, a, b, c, (int)x.get(i + 15), r4Shifts[9], -30611744);
            c = md5_i(c, d, a, b, (int)x.get(i + 6), r4Shifts[10], -1560198380);
            b = md5_i(b, c, d, a, (int)x.get(i + 13), r4Shifts[11], 1309151649);
            a = md5_i(a, b, c, d, (int)x.get(i + 4), r4Shifts[12], -145523070);
            d = md5_i(d, a, b, c, (int)x.get(i + 11), r4Shifts[13], -1120210379);
            c = md5_i(c, d, a, b, (int)x.get(i + 2), r4Shifts[14], 718787259);
            b = md5_i(b, c, d, a, (int)x.get(i + 9), r4Shifts[15], -343485551);
            b = md5_i(b, c, d, a, (int)x.get(i + 9), r4Shifts[15], -343485551);

            a = add(a, tempA);
            b = add(b, tempB);
            c = add(c, tempC);
            d = add(d, tempD);
        }

        String resstr = numToHex(a) + numToHex(b) + numToHex(c) + numToHex(d);
        return resstr;
    }

    /// <summary>
    /// 特殊的右移位操作，补0右移，如果是负数，需要进行特殊的转换处理（右移位）
    /// </summary>
    /// <param name="value"></param>
    /// <param name="pos"></param>
    /// <returns></returns>
    private static int MoveByte(int value, int pos)
    {
        if (value < 0)
        {
        	String s = Integer.toBinaryString(value);
//            String s = Convert.ToString(value, 2);    // 转换为二进制
            for (int i = 0; i < pos; i++)
            {
                s = "0" + s.substring(0, 31);
            }
           return Integer.valueOf(s,2);
//            return Convert.ToInt32(s, 2);            // 将二进制数字转换为数字
        }
        else
        {
            return value >> pos;
        }
    }

    private static String IsZero(byte b)
    {
        int a = b;
        if (a == 0)
        {
            return "";
        }
        else
        {
            return a+"";
        }
    }
//    
    public static void main(String[] args){
    	String code;
		try {
			code = FifaMd5Util.md5("asaa");
			System.out.println(code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}
