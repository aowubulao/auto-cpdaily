package com.neoniou.daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.neoniou.daily.constant.DailyApi;
import com.neoniou.daily.constant.Headers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 闲置状态，目前还没搞懂部分登录逻辑
 *
 * @author Neo.Zzj
 * @date 2020/7/16
 */
public class LoginRequest {

    private static String lt;
    private static String location;

    public static String login(String username, String password) {
        HttpResponse response = HttpRequest.get(DailyApi.SWU_INDEX)
                .header("User-Agent", Headers.USER_AGENT)
                .execute();

        List<HttpCookie> loginCookie = response.getCookies();
        // lt
        Document dom = Jsoup.parse(response.body());
        Elements elements = dom.getElementsByTag("input");
        for (Element element : elements) {
            if (element.toString().contains("lt")) {
                lt = element.toString().substring(38, 101);
                break;
            }
        }

        HttpResponse response2 = HttpRequest.post(DailyApi.SWU_LOGIN.replace("replace", loginCookie.get(0).toString()))
                .header("User-Agent", Headers.USER_AGENT)
                .header("cpdailyauthtype", "Login")
                .header("Upgrade-Insecure-Requests", "1")
                .cookie(String.valueOf(loginCookie))
                .form(generateLoginMap(username, password))
                .execute();
        System.out.println("response2: " + response2);

        loginCookie = response2.getCookies();

        HttpResponse response3 = HttpRequest.get(response2.header("Location"))
                .header("User-Agent", Headers.USER_AGENT)
                .cookie(String.valueOf(loginCookie))
                .execute();

        System.out.println("response3: " + response3);

        loginCookie = response3.getCookies();

        System.out.println(loginCookie.toString());


        return null;
    }

    private static Map<String, Object> generateLoginMap(String username, String password) {
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("username", username);
        loginMap.put("password", password);
        loginMap.put("lt", lt);
        loginMap.put("captchaResponse", "");
        loginMap.put("dllt", "mobileLogin");
        loginMap.put("execution", "e1s1");
        loginMap.put("_eventId", "submit");
        loginMap.put("rmShown", "1");

        return loginMap;
    }
}
