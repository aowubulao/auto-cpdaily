package daily.request;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import daily.constant.CpDaily;
import daily.constant.CpDailyApi;
import daily.constant.Headers;
import daily.pojo.MessageBox;
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

    private static String cookie;

    private static final String UNSIGNED_TASKS = "unSignedTasks";
    private static final String LEAVE_TASKS = "leaveTasks";
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

    /**
     * 获取当前时段的签到查寝信息
     *
     * @param activeAttendance 是否获取查寝信息
     * @return 信息列表
     */
    public List<MessageBox> getMessage(boolean activeAttendance) {
        JSONArray jsonArray = getResponseJson(CpDailyApi.SIGN_GET_MESSAGE);
        if (activeAttendance) {
            jsonArray.addAll(getResponseJson(CpDailyApi.ATTENDANCE_GET_MESSAGE));
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
                .contentType(Headers.APPLICATION_JSON)
                .cookie(cookie)
                .body("{\"pageSize\": 10,\"pageNumber\": 1}")
                .execute().body();
        JSONArray unsigned = JSONUtil.parseObj(responseBody).getJSONObject(DATAS).getJSONArray(UNSIGNED_TASKS);
        JSONArray leave = JSONUtil.parseObj(responseBody).getJSONObject(DATAS).getJSONArray(LEAVE_TASKS);
        unsigned.addAll(leave);
        return unsigned;
    }

    private String generateTime(String str) {
        return DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd") + " " + str).toString();
    }

    /**
     * 签到或查寝
     *
     * @param message 消息体
     * @param cpExtension 加密字符串
     * @return 是否成功
     */
    public boolean submitMessage(MessageBox message, String cpExtension) {
        String body;
        // 查寝
        if (message.getType() == 1) {
            log.info("===查寝===");
            body = CpDaily.SUBMIT_INFO.replace("siWid", message.getSignInstanceWid())
                    .replace("r1", longitude)
                    .replace("r2", latitude)
                    .replace("local", position);
        } else {
            log.info("===签到===");
            body = CpDaily.SIGN_INFO.replace("siWid", message.getSignInstanceWid())
                    .replace("itemId", getExtraFieldItemWid(message.getSignInstanceWid(), message.getSignWid()))
                    .replace("r1", longitude)
                    .replace("r2", latitude)
                    .replace("local", position);
        }
        return submitForm(
                body,
                cpExtension,
                message.getType() == 1 ? CpDailyApi.ATTENDANCE_SUBMIT_FORM : CpDailyApi.SIGN_SUBMIT_FORM
        );
    }

    /**
     * 提交消息
     *
     * @param body 签到或查寝的请求Body
     * @param cpExtension 加密字符串
     * @param api 签到或查寝的提交Api
     * @return 是否成功
     */
    public boolean submitForm(String body, String cpExtension, String api) {
        HttpResponse response = HttpRequest.post(api)
                .contentType(Headers.APPLICATION_JSON)
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
        String responseBody = HttpRequest.post(CpDailyApi.SIGN_GET_FORM)
                .contentType(Headers.APPLICATION_JSON)
                .cookie(cookie)
                .body("{\"signInstanceWid\":\"" + signInstanceWid + "\",\"signWid\":\"" + signWid + "\"}")
                .execute().body();

        String temp = responseBody.substring(responseBody.lastIndexOf("extraFieldItems"));
        String temp2 = temp.substring(temp.indexOf("wid"));
        return temp2.substring(5, temp2.indexOf(","));
    }
}