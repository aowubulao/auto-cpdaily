package daily;

import daily.constant.CpDaily;
import daily.pojo.MessageBox;
import daily.pojo.UserConfig;
import daily.request.InitialRequest;
import daily.request.LoginRequest;
import daily.request.ServerChanRequest;
import daily.request.SignRequest;
import daily.util.DesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @author Neo.Zzj
 * @date 2021/2/19
 */
@Slf4j
public class SignApplication {

    /**
     * Github Action需求的最少参数量
     */
    private static final int MIN_PARAMS = 6;

    private static final int ZERO = 0;


    public static void start(String... args) {
        UserConfig userConfig = readAndConfigArgs(args);
        if (userConfig == null) {
            return;
        }

        try {
            startApplication(userConfig);
        } catch (Exception e) {
            log.error("打卡失败，错误信息：", e);
            ServerChanRequest.sendMessage("今日校园自动打卡失败啦QAQ！",
                    "如果一直打卡失败的话，可以去github上看看有没有新版本，或者发送邮件到：me@neow.cc 提醒作者更新");
        }
    }

    private static void startApplication(UserConfig userConfig) throws Exception {
        log.info("程序启动... By Neo");

        // 初始化
        log.info("程序初始化中...");
        //判断是否需要读取配置文件初始化
        if (userConfig.getUsername() == null) {
            userConfig = InitialRequest.initialConfig();
            if (userConfig == null) {
                log.error("初始化失败");
                return;
            }
        }
        InitialRequest.initialServerChan(userConfig.getScKey());

        //登录
        String cookie = LoginRequest.login(userConfig.getUsername(), userConfig.getPassword());
        if (cookie == null) {
            log.error("用户名或密码错误, 登陆失败");
            return;
        } else {
            log.info("今日校园登陆成功");
        }

        //签到
        sign(userConfig, cookie);
    }

    private static void sign(UserConfig userConfig, String cookie) throws Exception {
        SignRequest signRequest = new SignRequest(userConfig.getLongitude(), userConfig.getLatitude(), userConfig.getPosition());

        signRequest.setCookie(cookie);
        List<MessageBox> messages = signRequest.getMessage(userConfig.getActiveAttendance());

        if (messages.size() < 1) {
            log.info("当前时间段未获取到签到信息");
        } else {
            log.info("获取到[{}]条签到信息", messages.size());

            int successNum = 0;

            String cpExtension = generateCpExtension(userConfig);
            for (MessageBox message : messages) {
                if (signRequest.submitMessage(message, cpExtension)) {
                    successNum++;
                    log.info("[{}]签到成功", message.getSignInstanceWid());
                } else {
                    log.info("[{}]签到失败", message.getSignInstanceWid());
                }
            }
            log.info("签到完成, 成功[{}]条, 失败[{}]条", successNum, messages.size() - successNum);
            ServerChanRequest.sendMessage("来自今日校园自动打卡的通知=w=",
                    "成功了" + successNum + "条" + "，失败了" + (messages.size() - successNum) + "条");
        }
    }

    private static String generateCpExtension(UserConfig userConfig) throws Exception {
        String replace = CpDaily.CP_EXTENSION.replace("r1", UUID.randomUUID().toString())
                .replace("r2", userConfig.getLongitude())
                .replace("r3", userConfig.getLatitude())
                .replace("r4", userConfig.getUsername());
        return DesUtil.encode(replace);
    }

    private static UserConfig readAndConfigArgs(String[] args) {
        int length = args.length;

        UserConfig userConfig = new UserConfig();
        // 参数大于0小于6则参数不完整，参数大于6则存在scKey，需要推送
        if (length < MIN_PARAMS && length > ZERO) {
            log.error("github仓库secret key数量设置错误！");
            return null;
        } else if (length >= MIN_PARAMS) {
            userConfig = new UserConfig(args[0], args[1], args[2], args[3], args[4], Boolean.parseBoolean(args[5]));
            if (length > MIN_PARAMS) {
                userConfig.setScKey(args[6]);
            }
        }
        return userConfig;
    }
}
