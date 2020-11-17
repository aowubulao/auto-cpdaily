package daily;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import daily.constant.CpDaily;
import daily.pojo.BaseInfo;
import daily.pojo.DailyApi;
import daily.pojo.MessageBox;
import daily.request.InitialRequest;
import daily.request.LoginRequest;
import daily.request.SignRequest;
import daily.util.DesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
@Slf4j
public class AutoDailyCp {

    public static DailyApi apis;

    public static BaseInfo info;

    public static void main(String[] args) throws Exception {
        new AutoDailyCp().mainHandler();
    }

    public void mainHandler() throws Exception {
        log.info("程序启动... By Neo");

        // 初始化
        log.info("程序初始化中...");
        if(!InitialRequest.initial()) {
            log.error("初始化失败");
            return;
        }

        String replace = CpDaily.CP_EXTENSION.replace("r1", UUID.randomUUID().toString())
                        .replace("r2", info.getLongitude())
                        .replace("r3", info.getLatitude())
                        .replace("r4", info.getUsername());
        String cpExtension = DesUtil.encode(replace);

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
                String extraFieldItemWid = signRequest.getExtraFieldItemWid(message.getSignInstanceWid(), message.getSignWid());

                if (signRequest.submitForm(message.getSignInstanceWid(), extraFieldItemWid, cpExtension)) {
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