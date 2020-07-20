package com.neoniou.daily.request;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.neoniou.daily.constant.CpDaily;
import com.neoniou.daily.constant.DailyApi;
import com.neoniou.daily.constant.Headers;
import com.neoniou.daily.pojo.MessageBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Slf4j
public class SignRequest {

    private static String   longitude;
    private static String   latitude;
    private static String   position;

    private static String   cookie;

    private static final String UNSIGNED_TASKS = "unSignedTasks";
    private static final String DATAS = "datas";
    private static final String NEW_MESSAGE = "msgsNew";
    private static final String SUCCESS = "SUCCESS";
    private static final String MESSAGE = "message";


    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        try {
            props.load(is);
            longitude = props.getProperty("longitude");
            latitude = props.getProperty("latitude");
            position = props.getProperty("position");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCookie(String cookies) {
        cookie = cookies;
    }

    public static List<MessageBox> getMessage() {
        String responseBody = HttpRequest.post(DailyApi.GET_MESSAGE)
                .header("Content-Type", "application/json")
                .header("Cookie", cookie)
                .body("{\"pageSize\": 10,\"pageNumber\": 1}")
                .execute().body();

        Object datas = JSONUtil.parseObj(JSONUtil.parseObj(responseBody).get(DATAS)).get(UNSIGNED_TASKS);
        JSONArray jsonArray = JSONUtil.parseArray(datas);

        List<MessageBox> messages = new ArrayList<>();
        for (Object msg : jsonArray) {
            MessageBox message = JSONUtil.toBean(msg.toString(), MessageBox.class);

            DateTime startTime = DateUtil.parse(generateTime(message.getRateTaskBeginTime()));
            DateTime endTime = DateUtil.parse(generateTime(message.getRateTaskEndTime()));
            if (message.getCurrentTime().after(startTime) && message.getCurrentTime().before(endTime)) {
                messages.add(message);
            }
        }

        return messages;
    }

    private static String generateTime(String str) {
        return DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd") + " " + str).toString();
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

        HttpResponse response = HttpRequest.post(DailyApi.SUBMIT_FORM)
                .header("Content-Type", Headers.CONTENT_TYPE)
                .header("User-Agent", Headers.USER_AGENT)
                .header("Cookie", cookie)
                .header("tenantId", CpDaily.TENANT_ID)
                .header("Cpdaily-Extension", CpDaily.CP_EXTENSION)
                .body(body)
                .execute();

        String responseBody = response.body();
        log.info("签到后的返回信息：{}", responseBody);

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

    public static String getExtraFieldItemWid(String signInstanceWid, String signWid) {
        String responseBody = HttpRequest.post(DailyApi.GET_FORM)
                .header("Content-Type", "application/json")
                .header("Cookie", cookie)
                .body("{\"signInstanceWid\":\"" + signInstanceWid + "\",\"signWid\":\"" + signWid + "\"}")
                .execute().body();

        System.out.println(responseBody);

        String temp = responseBody.substring(responseBody.lastIndexOf("extraFieldItems"));
        String temp2 = temp.substring(temp.indexOf("wid"));
        return temp2.substring(5, temp2.indexOf(","));
    }
}