package com.neoniou.daily;

import cn.hutool.core.date.DateUtil;
import com.neoniou.daily.pojo.MessageBox;
import com.neoniou.daily.request.LoginRequest;
import com.neoniou.daily.request.SignRequest;
import com.neoniou.daily.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
@Slf4j
public class AutoDailyCp {

    private static String username;
    private static String password;

    private static final int RETRY_TIME = 3;

    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        try {
            props.load(is);
            username = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        log.info("程序启动... By Neo");

        while (true) {
            timeLoop();

            for (int i = 1; true; i++) {
                try {
                    SignRequest.setCookie(LoginRequest.login(username, password));
                    break;
                } catch (Exception e) {
                    log.error("程序出错: ", e);
                    if (i == RETRY_TIME) {
                        return;
                    }
                    ThreadUtil.sleep(1000 * 60);
                }
            }

            List<MessageBox> messages = SignRequest.getMessage();
            if (messages.size() < 1) {
                log.info("当前时间段未获取到签到信息");
            } else {
                log.info("获取到[{}]条签到信息", messages.size());

                int successNum = submitForm(messages);
                log.info("签到完成, 成功[{}]条, 失败[{}]条", successNum, messages.size() - successNum);

                if (successNum == messages.size()) {
                    ThreadUtil.sleep(1000 * 60 * 60 * 6);
                }
            }

            ThreadUtil.sleep(1000 * 60 * 10);
        }
    }

    private static int submitForm(List<MessageBox> messages) {
        int successNum = 0;

        for (MessageBox message : messages) {
            String extraFieldItemWid = SignRequest.getExtraFieldItemWid(message.getSignInstanceWid(), message.getSignWid());

            if (SignRequest.submitForm(message.getSignInstanceWid(), extraFieldItemWid)) {
                successNum++;
                log.info("[{}]签到成功", message.getSignInstanceWid());
            } else {
                log.info("[{}]签到失败", message.getSignInstanceWid());
            }
        }

        return successNum;
    }

    /**
     * 到签到时间才开始获取信息
     */
    private static void timeLoop() {
        while (true) {
            int hour = DateUtil.hour(new Date(), true);
            if (hour >= 8 && hour < 12) {
                break;
            } else if (hour >= 19 && hour < 22) {
                break;
            }

            ThreadUtil.sleep(1000 * 60 * 10);
        }
    }
}
