package daily.request;

import cn.hutool.core.util.RandomUtil;
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
     * 随机坐标最小值
     */
    private static final int RANDOM_MIN = -50;

    /**
     * 随机坐标最大值
     */
    private static final int RANDOM_MAX = 50;

    private static final int MULTIPLIER = 1000000;

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
            userConfig.setLongitude(randomPosition(props.getProperty("longitude")));
            userConfig.setLatitude(randomPosition(props.getProperty("latitude")));
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

    /**
     * 位置坐标随机正负50
     *
     * @param positionStr 位置坐标字符串
     * @return 随机后的坐标
     */
    private static String randomPosition(String positionStr) {
        double position = Double.parseDouble(positionStr);

        double randomInt = RandomUtil.randomInt(RANDOM_MIN, RANDOM_MAX);
        position -= randomInt / MULTIPLIER;
        return String.format("%.6f", position);
    }

}