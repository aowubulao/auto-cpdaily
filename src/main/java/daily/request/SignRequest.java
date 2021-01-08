package daily.request;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import daily.AutoDailyCp;
import daily.pojo.MessageBox;
import daily.constant.CpDaily;
import daily.constant.Headers;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Slf4j
public class SignRequest {

    private final String longitude;
    private final String latitude;
    private final String position;

    private static String   cookie;

    private static final String UNSIGNED_TASKS = "unSignedTasks";
    private static final String SIGNED_TASKS = "signedTasks";
    private static final String DATAS = "datas";
    private static final String SUCCESS = "SUCCESS";
    private static final String MESSAGE = "message";
    private static final String ATTENDANCE = "查寝";

    public SignRequest(String longitude, String latitude, String position) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.position = position;
    }

    public void setCookie(String cookies) {
        cookie = cookies;
    }

    public List<MessageBox> getMessage() {
        JSONArray jsonArray = getResponseJson(AutoDailyCp.info.getSignGetMessage());
        if (AutoDailyCp.info.getActiveAttendance()) {
            jsonArray.addAll(getResponseJson(AutoDailyCp.info.getAttendanceGetMessage()));
        }

        List<MessageBox> messages = new ArrayList<>();
        for (Object msg : jsonArray) {
            MessageBox message = JSONUtil.toBean(msg.toString(), MessageBox.class);
            if (msg.toString().contains(ATTENDANCE)) {
                message.setType(1);
            } else {
                message.setType(0);
            }
            DateTime startTime = DateUtil.parse(generateTime(message.getRateTaskBeginTime()));
            DateTime endTime = DateUtil.parse(generateTime(message.getRateTaskEndTime()));
            if (message.getCurrentTime().after(startTime) && message.getCurrentTime().before(endTime)) {
                messages.add(message);
            }
        }

        return messages;
    }

    private static JSONArray getResponseJson(String api) {
        String responseBody = HttpRequest.post(api)
                .header("Content-Type", "application/json")
                .header("Cookie", cookie)
                .body("{\"pageSize\": 10,\"pageNumber\": 1}")
                .execute().body();

        return JSONUtil.parseObj(responseBody).getJSONObject(DATAS).getJSONArray(UNSIGNED_TASKS);
    }

    private String generateTime(String str) {
        return DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd") + " " + str).toString();
    }

    public boolean submitMessage(MessageBox message, String cpExtension) {
        String body;
        // 查寝
        if (message.getType() == 1) {
            body = CpDaily.SUBMIT_INFO.replace("siWid", message.getSignWid())
                    .replace("r1", longitude)
                    .replace("r2", latitude)
                    .replace("local", position);
        } else {
            body = CpDaily.SIGN_INFO.replace("siWid", message.getSignWid())
                    .replace("itemId", getExtraFieldItemWid(message.getSignInstanceWid(), message.getSignWid()))
                    .replace("r1", longitude)
                    .replace("r2", latitude)
                    .replace("local", position);
        }
        return submitForm (
                body,
                cpExtension,
                message.getType() == 1 ? AutoDailyCp.info.getAttendanceSubmitForm() : AutoDailyCp.info.getSignSubmitForm()
        );
    }

    public boolean submitForm(String body, String cpExtension, String api) {
        HttpResponse response = HttpRequest.post(api)
                .header("Content-Type", Headers.CONTENT_TYPE)
                .header("tenantId", CpDaily.TENANT_ID)
                .header("Cpdaily-Extension", cpExtension)
                .body(body)
                .execute();

        String responseBody = response.body();
        log.info("签到后的返回信息：[{}]", responseBody);

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

    public String getExtraFieldItemWid(String signInstanceWid, String signWid) {
        String responseBody = HttpRequest.post(AutoDailyCp.info.getSignGetForm())
                .header("Content-Type", "application/json")
                .header("Cookie", cookie)
                .body("{\"signInstanceWid\":\"" + signInstanceWid + "\",\"signWid\":\"" + signWid + "\"}")
                .execute().body();

        String temp = responseBody.substring(responseBody.lastIndexOf("extraFieldItems"));
        String temp2 = temp.substring(temp.indexOf("wid"));
        return temp2.substring(5, temp2.indexOf(","));
    }
}