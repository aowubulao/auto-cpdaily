package daily;

import daily.constant.CpDaily;
import daily.pojo.BaseInfo;
import daily.pojo.MessageBox;
import daily.request.InitialRequest;
import daily.request.LoginRequest;
import daily.request.ServerChanRequest;
import daily.request.SignRequest;
import daily.util.DesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
@Slf4j
public class AutoDailyCp {

    public static BaseInfo info;

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));
        System.out.println(args.length);
        System.out.println("-=-=");
        for (String s : args) {
            System.out.println(s);
        }

        //new AutoDailyCp().mainHandler(new KeyValueClass());
    }

    public void mainHandler(KeyValueClass kv) throws Exception {
        try {
            startApplication();
        } catch (Exception e) {
            log.error("打卡失败，错误信息：", e);
            ServerChanRequest.sendMessage("今日校园自动打卡失败啦QAQ！",
                    "如果一直打卡失败的话，可以去github上看看有没有新版本，或者发送邮件到：me@neow.cc 提醒作者更新");
        }
    }

    private static void startApplication() throws Exception {
        log.info("程序启动... By Neo");

        // 初始化
        log.info("程序初始化中...");
        if(!InitialRequest.initial()) {
            log.error("初始化失败");
            return;
        }

        String cpExtension = generateCpExtension();
        String cookie = LoginRequest.login(info.getUsername(), info.getPassword());

        if (cookie == null) {
            log.error("用户名或密码错误, 登陆失败");
            return;
        }
        log.info("今日校园登陆成功");
        SignRequest signRequest = new SignRequest(info.getLongitude(), info.getLatitude(), info.getPosition());

        signRequest.setCookie(cookie);
        List<MessageBox> messages = signRequest.getMessage();
        if (messages.size() < 1) {
            log.info("当前时间段未获取到签到信息");
        } else {
            log.info("获取到[{}]条签到信息", messages.size());

            int successNum = 0;

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

    private static String generateCpExtension() throws Exception {
        String replace = CpDaily.CP_EXTENSION.replace("r1", UUID.randomUUID().toString())
                .replace("r2", info.getLongitude())
                .replace("r3", info.getLatitude())
                .replace("r4", info.getUsername());
        return DesUtil.encode(replace);
    }

    private static class KeyValueClass {
    }
}