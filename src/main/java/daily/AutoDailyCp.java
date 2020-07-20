package daily;

import cn.hutool.core.date.DateTime;
import daily.pojo.MessageBox;
import daily.request.LoginRequest;
import daily.request.SignRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
@Slf4j
public class AutoDailyCp {

    public static void mainHandler(KeyValueClass kv) {
        log.info("程序启动... By Neo");

        System.out.println(new DateTime());

        String cookie = LoginRequest.login(kv.getUsername(), kv.getPassword());

        System.out.println(cookie);

        SignRequest signRequest = new SignRequest(kv.getLongitude(), kv.getLatitude(), kv.getPosition());

        signRequest.setCookie(cookie);
        List<MessageBox> messages = signRequest.getMessage();
        if (messages.size() < 1) {
            log.info("当前时间段未获取到签到信息");
        } else {
            log.info("获取到[{}]条签到信息", messages.size());

            int successNum = 0;

            for (MessageBox message : messages) {
                String extraFieldItemWid = signRequest.getExtraFieldItemWid(message.getSignInstanceWid(), message.getSignWid());

                if (signRequest.submitForm(message.getSignInstanceWid(), extraFieldItemWid)) {
                    successNum++;
                    log.info("[{}]签到成功", message.getSignInstanceWid());
                } else {
                    log.info("[{}]签到失败", message.getSignInstanceWid());
                }
            }
            log.info("签到完成, 成功[{}]条, 失败[{}]条", successNum, messages.size() - successNum);
        }
    }
}