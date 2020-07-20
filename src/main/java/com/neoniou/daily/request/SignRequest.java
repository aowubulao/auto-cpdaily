package com.neoniou.daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.neoniou.daily.constant.DailyApi;
import com.neoniou.daily.constant.Headers;
import com.neoniou.daily.pojo.MessageBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Slf4j
public class SignRequest {

    private static String   cookie;
    private static String   tenantId;
    private static String   cpVersion;
    private static String   accessToken;
    private static String   appId;
    private static String   longitude;
    private static String   latitude;
    private static String   position;
    private static String   requestBody;
    private static String   modAuthCAS;

    private static final String NEW_MESSAGE = "msgsNew";
    private static final String SUCCESS = "SUCCESS";
    private static final String MESSAGE = "message";


    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        try {
            props.load(is);
            cookie = props.getProperty("cookie");
            tenantId = props.getProperty("tenantId");
            cpVersion = props.getProperty("cpVersion");
            accessToken = props.getProperty("accessToken");
            appId = props.getProperty("appId");
            longitude = props.getProperty("longitude");
            latitude = props.getProperty("latitude");
            position = props.getProperty("position");
            requestBody = props.getProperty("requestBody");
            modAuthCAS = props.getProperty("modAuthCAS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageBox getMessage() {
        String responseBody = HttpRequest.post(DailyApi.GET_MESSAGE)
                .header("User-Agent", Headers.USER_AGENT)
                .header("accessToken", accessToken)
                .header("appId", appId)
                .header("Cookie", cookie)
                .body(requestBody)
                .execute().body();
        System.out.println(responseBody);
        JSONObject resJson = JSONUtil.parseObj(responseBody);
        JSONArray msgArray = JSONUtil.parseArray(resJson.get(NEW_MESSAGE));
        return JSONUtil.toBean((JSONObject) msgArray.get(msgArray.size() - 1), MessageBox.class);
    }

    public static void signPage(String mobileUrl) {

    }

    public static boolean submitForm(String signInstanceWid, String extraFieldItemWid) {
        String signInfo = "{\"signInstanceWid\":\"siWid\",\"longitude\":r1,\"latitude\":r2,\"isMalposition\":1," +
                "\"abnormalReason\":\"\",\"signPhotoUrl\":\"\",\"position\":\"local\"," +
                "\"isNeedExtra\":1,\"" +
                "extraFieldItems\":[{\"extraFieldItemValue\":\"正常，<37.2℃\",\"extraFieldItemWid\":itemId}]}";
        String body = signInfo.replace("siWid", signInstanceWid)
                .replace("itemId", extraFieldItemWid)
                .replace("r1", longitude)
                .replace("r2", latitude)
                .replace("local", position);

        System.out.println(body);

        HttpResponse response = HttpRequest.post(DailyApi.SUBMIT_FORM)
                .header("Content-Type", Headers.CONTENT_TYPE)
                .header("User-Agent", Headers.USER_AGENT)
                .header("Cookie", cookie)
                .header("tenantId", tenantId)
                .header("Cpdaily-Extension", cpVersion)
                .body(body)
                .execute();

        String responseBody = response.body();
        log.info("提交后的返回体：{}", responseBody);

        boolean flag = false;
        try {
            JSONObject resJson = JSONUtil.parseObj(responseBody);
            if (SUCCESS.equals(resJson.get(MESSAGE))) {
                flag = true;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return flag;
    }

    public static boolean heartDo() {
        HttpResponse response = HttpRequest.post("https://swu.cpdaily.com/wec-counselor-sign-apps/stu/sign/detailSignInstance")
                .header("Content-Type", "application/json")
                .header("Cookie", modAuthCAS)
                .body("{\"signInstanceWid\":\"4277\",\"signWid\":\"449717\"}")
                .execute();
        if (response.body().charAt(0) == '{') {
            log.info("检查一次Cookie，登录状态保持");
            return true;
        } else {
            log.info("检查一次Cookie，登录状态失效");
            return false;
        }
    }

    public static String getExtraFieldItemWid(String signInstanceWid, String signWid) {
        String responseBody = HttpRequest.post(DailyApi.GET_FORM)
                .header("Content-Type", "application/json")
                .header("Cookie", modAuthCAS)
                .body("{\"signInstanceWid\":\"" + signInstanceWid + "\",\"signWid\":\"" + signWid + "\"}")
                .execute().body();

        String temp = responseBody.substring(responseBody.lastIndexOf("extraFieldItems"));
        String temp2 = temp.substring(temp.indexOf("wid"));
        return temp2.substring(5, temp2.indexOf(","));
    }
}