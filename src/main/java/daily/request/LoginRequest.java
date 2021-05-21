package daily.request;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import daily.constant.CpDailyApi;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Neo.Zzj
 * @date 2020/7/20
 */
public class LoginRequest {

    private static final int OK = 200;

    private static String lt;

    /**
     * 根据用户名密码登录
     *
     * @param username 用户名（学号）
     * @param password 密码
     * @return 登录后的Cookie, 返回null为登录失败
     */
    public static String login(String username, String password) {
        HttpResponse ltRes = HttpRequest.post(CpDailyApi.SWU_LT)
                .body("lt", "")
                .execute();

        String lt = JSONUtil.parseObj(ltRes.body()).getJSONObject("result").getStr("_lt");

        HttpResponse loginRes = HttpRequest.post(CpDailyApi.SWU_DO_LOGIN)
                .form(genLoginMap(username, password, lt))
                .cookie(parseCookie(ltRes.getCookies()))
                .execute();

        HttpResponse doLoginRes = HttpRequest.get(CpDailyApi.SWU_AUTH_LOGIN)
                .cookie(parseCookie(loginRes.getCookies()))
                .execute();

        HttpResponse resultRes = HttpRequest.get(doLoginRes.header(Header.LOCATION))
                .cookie(parseCookie(doLoginRes.getCookies()))
                .execute();

        return parseCookie(resultRes.getCookies());
    }

    private static String parseCookie(List<HttpCookie> cookies) {
        StringBuilder sb = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            sb.append(cookie.toString()).append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static Map<String, Object> genLoginMap(String username, String password, String lt) {
        Map<String, Object> loginMap = new HashMap<>(16);
        loginMap.put("username", username);
        loginMap.put("password", password);
        loginMap.put("mobile", "");
        loginMap.put("dllt", "");
        loginMap.put("captcha", "");
        loginMap.put("rememberMe", "false");
        loginMap.put("lt", lt);
        return loginMap;
    }
}
