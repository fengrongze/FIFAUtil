package utils;

import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.bean.SoftCookie;
import com.softisland.common.utils.bean.SoftHeader;
import com.softisland.common.utils.bean.SoftHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liwx on 16/3/23.
 */
public class HttpClientTest {
    public static void main(String[] args)throws Exception {
        testAcceptTrade();
    }

    private static void mulThread()throws Exception{
        List<String> list = new ArrayList<>();
        for(int i=0;i<50;i++){
            Runnable runnable = ()-> {
                SoftHttpResponse response = null;
                try {
                    for(int j=0;j<1;j++){
                        try {
                            TimeUnit.MILLISECONDS.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        response = HttpClientUtil.getSteamStock("76561198264327736");
                        if(null == response.getContent()){
                            list.add("1");
                        }
                        System.out.println(Thread.currentThread().getName()+"@@@@@@@@@"+response.getContent());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            Thread rr = new Thread(runnable);
            rr.join();
            rr.start();
        }

        System.out.println("+++++++++++"+list.size());
    }

    private static void testStock()throws Exception{
        //76561198276810732
        SoftHttpResponse response = HttpClientUtil.getSteamStock("76561198276810732");
        System.out.println(response.getContent());
    }

    public static void testAcceptTrade()throws Exception{
        String url = "https://steamcommunity.com/tradeoffer/1270000879/accept";
        Map<String,String> map = new HashMap<>();
        map.put("sessionid","39dd9d3830d14b156882a36a");
        map.put("serverid","1");
        map.put("tradeofferid","1270000879");
        map.put("partner","1233333333333");
        /*bCompletedTradeOfferTutorial=true;
        steamMachineAuth76561198246267740=FEBA4339821F486EE5DD4A7BA755860AF28CA5A9 __utma=268881843.1363869263.1460381078.1461306572.1461549435.8;
        __utmz=268881843.1460381078.1.1.utmcsr=store.steampowered.com|utmccn=(referral)|utmcmd=referral|utmcct=/;
        steamMachineAuth76561198293250753=20D5215BB69F110CF71DE917B5952566BE5A2BC4;
        recentlyVisitedAppHubs=730;
        steamMachineAuth76561198126297883=0DF1CD59EFF6FA0E4ECFEB1F0C47276E3E5598AC;
        steamMachineAuth76561198292741592=113B783DC69D456AA9B61520EB3ED951ACEE6034;
        steamMachineAuth76561198292592726=C2FDF418A799CA4F5A1E875B18964B2ADD7D0FE8;
        sessionid=39dd9d3830d14b156882a36a;
        steamCountry=CN%7C782be65066aff833853feb3d472d9f55;
        steamLogin=76561198289233394%7C%7C5AFE7E62024B2718B268ECDB34B5021ADB017BF7;
        steamLoginSecure=76561198289233394%7C%7C84C98768DDFF60E2792A3CCA27AA02883D1795B6;
        steamMachineAuth76561198289233394=167DA3532606E604DC40BD4E9EC8713FFC018F4B;
        webTradeEligibility=%7B%22allowed%22%3A0%2C%22reason%22%3A32%2C%22allowed_at_time%22%3A1466763498%2C%22steamguard_required_days%22%3A15%2C%22sales_this_year%22%3A0%2C%22max_sales_per_year%22%3A200%2C%22forms_requested%22%3A0%2C%22new_device_cooldown_days%22%3A7%7D;
        tsTradeOffersLastRead=1464171427;
        _ga=GA1.2.1363869263.1460381078; timezoneOffset=28800,0*/
        SoftCookie softCookie = new SoftCookie("sessionid","39dd9d3830d14b156882a36a","steamcommunity.com");
        SoftCookie softCookie1 = new SoftCookie("steamLogin","76561198289233394%7C%7C5AFE7E62024B2718B268ECDB34B5021ADB017BF7","steamcommunity.com");
        SoftCookie softCookie2 = new SoftCookie("steamLoginSecure","76561198289233394%7C%7C84C98768DDFF60E2792A3CCA27AA02883D1795B6","steamcommunity.com");

       /* SoftCookie softCookie3 = new SoftCookie("bCompletedTradeOfferTutorial","true","steamcommunity.com");
        SoftCookie softCookie5 = new SoftCookie("steamMachineAuth76561198246267740","FEBA4339821F486EE5DD4A7BA755860AF28CA5A9","steamcommunity.com");
        SoftCookie softCookie6 = new SoftCookie("__utma","268881843.1363869263.1460381078.1461306572.1461549435.8","steamcommunity.com");
        SoftCookie softCookie7 = new SoftCookie("__utmz","268881843.1460381078.1.1.utmcsr=store.steampowered.com|utmccn=(referral)|utmcmd=referral|utmcct=/","steamcommunity.com");
        SoftCookie softCookie8 = new SoftCookie("steamMachineAuth76561198293250753","20D5215BB69F110CF71DE917B5952566BE5A2BC4","steamcommunity.com");
        SoftCookie softCookie9 = new SoftCookie("recentlyVisitedAppHubs","730","steamcommunity.com");
        SoftCookie softCookie10 = new SoftCookie("steamMachineAuth76561198126297883","0DF1CD59EFF6FA0E4ECFEB1F0C47276E3E5598AC","steamcommunity.com");
        SoftCookie softCookie11 = new SoftCookie("steamMachineAuth76561198292741592","113B783DC69D456AA9B61520EB3ED951ACEE6034","steamcommunity.com");
        SoftCookie softCookie12 = new SoftCookie("steamMachineAuth76561198292592726","C2FDF418A799CA4F5A1E875B18964B2ADD7D0FE8","steamcommunity.com");
        SoftCookie softCookie13 = new SoftCookie("steamMachineAuth76561198289233394","167DA3532606E604DC40BD4E9EC8713FFC018F4B","steamcommunity.com");
        SoftCookie softCookie14 = new SoftCookie("webTradeEligibility","%7B%22allowed%22%3A0%2C%22reason%22%3A32%2C%22allowed_at_time%22%3A1466763498%2C%22steamguard_required_days%22%3A15%2C%22sales_this_year%22%3A0%2C%22max_sales_per_year%22%3A200%2C%22forms_requested%22%3A0%2C%22new_device_cooldown_days%22%3A7%7D","steamcommunity.com");
        SoftCookie softCookie16 = new SoftCookie("_ga","GA1.2.1363869263.1460381078; timezoneOffset=28800,0","steamcommunity.com");*/

        SoftCookie softCookie15 = new SoftCookie("tsTradeOffersLastRead","1270000879","steamcommunity.com");

        SoftHeader header = new SoftHeader("Referer","https://steamcommunity.com/tradeoffer/1270000879/");
        SoftHttpResponse response = HttpClientUtil.postParamsToUrl(url,map,
                new SoftCookie[]{softCookie,softCookie1,softCookie2,softCookie15,},new SoftHeader[]{header});
        System.out.println(response.getContent());
    }
}
