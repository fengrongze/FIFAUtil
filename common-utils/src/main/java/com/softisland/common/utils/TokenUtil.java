package com.softisland.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;

/**
 * Created by liwx on 2015/10/20.
 */
public final class TokenUtil {
    private static final String ENCODED_PREFIX = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..";
    private static final String jwkJson = "{\"kty\":\"oct\",\"k\":\"Fdh9u9rINxfivbrianbbVT1u232VQBZYKx1HGAGPt2I\"}";
    private static JsonWebKey jwk;
    static{
        try {
            jwk = JsonWebKey.Factory.newJwk(jwkJson);

        }catch (Exception e){}
    }

    public static void main(String[] args)throws Exception {
        long start = System.currentTimeMillis();
        String code = "aaUnPGXppD";
        String encode = jweEncryption(code);
        System.out.println(encode);
        String deStr = "5tDSRxr8BYQtZvPIDwShow.w0w1HwX_2wV-LYORqBtRIQ.dK3zSgcoQxTilgtrSVcD0A";
        String de = jweDecryption(deStr.replace("\\n",""));
        System.out.println(de);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(de.equals(code));
    }



    /**
     * 加密生成
     * @param payload
     * @return
     * @throws Exception
     */
    public static String jweEncryption(String payload)throws Exception{

        JsonWebEncryption jwe = new JsonWebEncryption();
        //jwe.setPlaintext(payload+System.currentTimeMillis());
        jwe.setPlaintext(payload);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(jwk.getKey());

        return jwe.getCompactSerialization().substring(49);
    }

    /**
     * 解密
     * @param decodeStr
     * @return
     * @throws Exception
     */
    public static String jweDecryption(String decodeStr){

        try{
            JsonWebEncryption receiverJwe = new JsonWebEncryption();
            receiverJwe.setCompactSerialization(ENCODED_PREFIX+decodeStr);
            receiverJwe.setKey(jwk.getKey());
            //获取，验证是否成功
            return receiverJwe.getPlaintextString();
        }catch (JoseException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static boolean validateToken(String token){
        if(StringUtils.isEmpty(jweDecryption(token))){
            return false;
        }else{
            return true;
        }
    }

}
