package daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import daily.AutoDailyCp;
import daily.pojo.BaseInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/10/31
 */
@Slf4j
public class InitialRequest {

    private static final String GET_VERSION = "https://api.neoniou.com/client/daily/v2/getVersion";

    private static final String UPDATE_API = "https://api.neoniou.com/client/daily/v2/getApi";

    private static final String VERSION = "apiVersion";

    private static final String MSG = "authorMsg";

    private static final String CP_EXTENSION = "{\"systemName\":\"android\",\"systemVersion\":\"10\",\"model\":\"Mi 10\"" +
            ",\"deviceId\":\"r1\",\"appVersion\":" +
            "\"8.0.8\",\"lon\":r2,\"lat\":r3,\"userId\":\"r4\"}";

    public static boolean initial() {
        AutoDailyCp.info = new BaseInfo();

        return readDailyProps() && initialApi();
    }

    private static boolean readDailyProps() {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        try {
            props.load(is);
            AutoDailyCp.info.setUsername(props.getProperty("username"));
            AutoDailyCp.info.setPassword(props.getProperty("password"));
            AutoDailyCp.info.setLongitude(props.getProperty("longitude"));
            AutoDailyCp.info.setLatitude(props.getProperty("latitude"));
            AutoDailyCp.info.setPosition(props.getProperty("position"));

            return true;
        } catch (IOException e) {
            log.info("读取daily.properties错误: ", e);
            return false;
        }
    }

    private static boolean initialApi() {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("api.properties");
        try {
            props.load(is);
            AutoDailyCp.info.setApiVersion(props.getProperty("apiVersion"));
            // 检查版本
            if (isNeedUpdate()) {
                // 更新
                updateApi();
            }

            // 读取
            AutoDailyCp.info.setSwuIndex(props.getProperty("swuIndex"));
            AutoDailyCp.info.setSwuLogin(props.getProperty("swuLogin"));

            AutoDailyCp.info.setSignGetMessage(props.getProperty("signGetMessage"));
            AutoDailyCp.info.setSignGetForm(props.getProperty("signGetForm"));
            AutoDailyCp.info.setSignSubmitForm(props.getProperty("signSubmitForm"));
            AutoDailyCp.info.setAttendanceGetMessage(props.getProperty("attendanceGetMessage"));
            AutoDailyCp.info.setAttendanceGetForm(props.getProperty("attendanceGetForm"));
            AutoDailyCp.info.setAttendanceSubmitForm(props.getProperty("attendanceSubmitForm"));

            return true;
        } catch (IOException e) {
            log.info("读取api.properties错误: ", e);
            return false;
        }
    }

    private static boolean isNeedUpdate() {
        try {
            String body = HttpRequest.get(GET_VERSION)
                    .execute()
                    .body();
            JSONObject resInfo = JSONUtil.parseObj(body);
            log.info("作者有句话想说：[{}]", resInfo.get(MSG));

            String version = resInfo.get(VERSION).toString();
            if (Integer.parseInt(version) > Integer.parseInt(AutoDailyCp.info.getApiVersion())) {
                log.info("有新版Api，准备进行更新: [{}]->[{}]", AutoDailyCp.info.getApiVersion(), version);
                return true;
            } else {
                log.info("当前Api已是最新版, 版本: [{}]", version);
                return false;
            }
        } catch (Exception e) {
            log.info("获取版本失败: ", e);
            return false;
        }
    }

    private static void updateApi() {
        BaseInfo newInfo = null;

        try {
            String body = HttpRequest.get(UPDATE_API)
                    .execute()
                    .body();

            newInfo = JSONUtil.toBean(body, BaseInfo.class);
        } catch (Exception e) {
            log.info("获取Api 失败: ", e);
        }
        if (newInfo == null) {
            return;
        }

        Properties props = new Properties();
        try {
            props.setProperty("swuIndex", newInfo.getSwuIndex());
            props.setProperty("swuLogin", newInfo.getSwuLogin());

            props.setProperty("signGetMessage", newInfo.getSignGetMessage());
            props.setProperty("signGetForm", newInfo.getSignGetForm());
            props.setProperty("signSubmitForm", newInfo.getSignSubmitForm());

            props.setProperty("attendanceGetMessage", newInfo.getSwuIndex());
            props.setProperty("attendanceGetForm", newInfo.getAttendanceGetForm());
            props.setProperty("attendanceSubmitForm", newInfo.getAttendanceSubmitForm());

            props.setProperty("apiVersion", newInfo.getApiVersion());

            String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("api.properties")).toString();
            props.store(new FileOutputStream(path.substring(6)), null);
        } catch (Exception e) {
            log.info("写入api.properties失败: ", e);
        }
    }
}