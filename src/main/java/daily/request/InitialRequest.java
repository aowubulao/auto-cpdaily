package daily.request;

import daily.AutoDailyCp;
import daily.pojo.BaseInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/10/31
 */
@Slf4j
public class InitialRequest {

    public static boolean initial() {
        if (AutoDailyCp.info == null) {
            AutoDailyCp.info = new BaseInfo();
            return readDailyProps() && initialApi();
        } else {
            return initialApi();
        }
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
            AutoDailyCp.info.setScKey(props.getProperty("scKey"));
            AutoDailyCp.info.setActiveAttendance(true);
            AutoDailyCp.info.setActiveAttendance(Boolean.parseBoolean(props.getProperty("activeAttendance")));
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
}