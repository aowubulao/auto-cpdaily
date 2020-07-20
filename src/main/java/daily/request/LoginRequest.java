package daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import daily.constant.DailyApi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neo.Zzj
 * @date 2020/7/20
 */
public class LoginRequest {

    private static String lt;

    public static String login(String username, String password) {
        // 首页信息
        HttpResponse indexRes = HttpRequest.get(DailyApi.SWU_INDEX)
                .header("Cookie", "org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh_CN")
                .execute();

        System.out.println("首页信息：" + indexRes);
        lt = getLt(indexRes.body());
        Map<String, Object> loginMap = generateLoginMap(username, password);

        // 替换登录 url
        List<HttpCookie> cookies = indexRes.getCookies();
        String sessionId = cookies.get(0).toString();
        String loginUrl = DailyApi.SWU_LOGIN.replace("sessionId", sessionId);

        // 登录步骤 1
        HttpResponse loginRes = HttpRequest.post(loginUrl)
                .cookie(cookies.toString())
                .form(loginMap)
                .execute();
        System.out.println("loginRes: " + loginRes);
        cookies = loginRes.getCookies();

        // 跳转最终获取 Cookie
        HttpResponse moveRes = HttpRequest.get(loginRes.header("Location"))
                .cookie(cookies.toString())
                .execute();
        cookies = moveRes.getCookies();

        return handleCookie(cookies);
    }

    private static String handleCookie(List<HttpCookie> cookies) {
        String cas = cookies.get(0).toString();
        String temp = cookies.get(1).toString();
        String acw = temp.substring(0, temp.indexOf(";")).replaceAll("\"", "");

        return cas + ";" + acw;
    }

    private static String getLt(String document) {
        Document dom = Jsoup.parse(document);
        Elements elements = dom.getElementsByTag("input");
        for (Element element : elements) {
            if (element.toString().contains("lt")) {
                lt = element.toString().substring(38, 101);
                return lt;
            }
        }
        return null;
    }

    private static Map<String, Object> generateLoginMap(String username, String password) {
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("username", username);
        loginMap.put("password", password);
        loginMap.put("rememberMe", "on");
        loginMap.put("lt", lt);
        loginMap.put("dllt", "userNamePasswordLogin");
        loginMap.put("execution", "e1s1");
        loginMap.put("_eventId", "submit");
        loginMap.put("rmShown", "1");

        return loginMap;
    }
}
