package utils;

import com.alibaba.fastjson.JSON;
import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.ShortUrlUtils;
import com.softisland.common.utils.Utils;
import com.softisland.common.utils.bean.SoftHttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by liwx on 2016/4/13.
 */
public class ShortUrlUtilsTest {
    static Map<String,Boolean> map = new HashMap<>();
    static Random random = new Random();
    public static void main(String[] args) {
        System.out.println(Utils.getUuid());

    }

    private static String getUrl(String str){
        String url = "http://dwz.cn/create.php";
        Map<String,String> map = new HashMap<>();
        map.put("url","http://dwz.cn/"+str);
        try {
            SoftHttpResponse response = HttpClientUtil.postParamsToUrl(url,map,null);
            String shortUrl = JSON.parseObject(response.getContent()).getString("tinyurl");
            String code = shortUrl.substring(shortUrl.length()-6);
            System.out.println(response.getContent());
            return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
