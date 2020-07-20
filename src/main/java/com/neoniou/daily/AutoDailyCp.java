package com.neoniou.daily;

import cn.hutool.core.date.DateUtil;
import com.neoniou.daily.pojo.MessageBox;
import com.neoniou.daily.request.SignRequest;
import com.neoniou.daily.util.MailUtil;
import com.neoniou.daily.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
@Slf4j
public class AutoDailyCp {

    private static volatile boolean flag = true;

    public static void main(String[] args) {
        log.info("程序启动... By Neo");
        //保持连接
        /*ThreadUtil.run(() -> {
            while (flag) {
                flag = SignRequest.heartDo();
                ThreadUtil.sleep(1000 * 60 * 5);
            }
        });*/

        while (flag) {
            timeLoop();

            MessageBox message;
            try {
                message = SignRequest.getMessage();
            } catch (Exception e) {
                log.error("定时任务发生错误: ", e);
                new MailUtil().sendMessage("定时任务发生错误", "定时任务发生错误");
                break;
            }

            assert message != null;
            if (!message.getIsHandled() && flag) {
                log.info("获取到新的签到信息");

                String signInstanceWid = getSignInstanceWid(message);
                String signWid = getSignWid(message);
                String extraFieldItemWid = SignRequest.getExtraFieldItemWid(signInstanceWid, signWid);

                if (SignRequest.submitForm(signInstanceWid, extraFieldItemWid)) {
                    log.info("签到成功");
                    ThreadUtil.sleep(1000 * 60 * 60 * 6);
                } else {
                    log.info("签到失败...");
                    break;
                }
            } else {
                log.info("获取一次，未获取到签到信息");
            }

            // 20 分钟获取一次
            ThreadUtil.sleep(1000 * 60 * 20);
        }

        ThreadUtil.shutdown();
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

    private static String getSignWid(MessageBox message) {
        String s = message.getMobileUrl();
        String s2 = s.substring(s.indexOf("Wid="));
        return s2.substring(4, s2.indexOf("&"));
    }

    private static String getSignInstanceWid(MessageBox message) {
        String s = message.getMobileUrl();
        String s2 = s.substring(s.lastIndexOf("Wid="));
        return s2.substring(4, s2.indexOf("&"));
    }
}
