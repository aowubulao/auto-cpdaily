package daily.request;

import daily.pojo.UserConfig;
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

    /**
     * 初始化配置文件
     *
     * @return UserConfig
     */
    public static UserConfig initialConfig() {
        return readDailyProps();
    }

    public static void initialServerChan(String scKey) {
        ServerChanRequest.initial(scKey);
    }

    private static UserConfig readDailyProps() {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        try {
            UserConfig userConfig = new UserConfig();
            props.load(is);
            userConfig.setUsername(props.getProperty("username"));
            userConfig.setPassword(props.getProperty("password"));
            userConfig.setLongitude(props.getProperty("longitude"));
            userConfig.setLatitude(props.getProperty("latitude"));
            userConfig.setPosition(props.getProperty("position"));
            userConfig.setScKey(props.getProperty("scKey"));
            userConfig.setActiveAttendance(true);
            userConfig.setActiveAttendance(Boolean.parseBoolean(props.getProperty("activeAttendance")));
            return userConfig;
        } catch (IOException e) {
            log.info("读取daily.properties错误: ", e);
            return null;
        }
    }
}