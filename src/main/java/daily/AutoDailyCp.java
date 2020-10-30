package daily;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import daily.constant.CpDaily;
import daily.pojo.DailyApi;
import daily.pojo.MessageBox;
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

    public static void main(String[] args) throws Exception {
        new AutoDailyCp().mainHandler(new KeyValueClass());
    }

    public void mainHandler(KeyValueClass kv) throws Exception {
        log.info("程序启动... By Neo");
        kv = getProps();

        String replace = CpDaily.CP_EXTENSION.replace("r1", UUID.randomUUID().toString())
                        .replace("r2", kv.getLongitude())
                        .replace("r3", kv.getLatitude())
                        .replace("r4", kv.getUsername());
        String cpExtension = DesUtil.encode(replace);

        String cookie = LoginRequest.login(kv.getUsername(), kv.getPassword());
        if (cookie == null) {
            log.error("登录账户密码错误！");
            return;
        }
        log.info("此次Cookie : [{}]", cookie);
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

    private KeyValueClass getProps() {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("daily.properties");
        KeyValueClass kv = new KeyValueClass();
        try {
            props.load(is);
            kv.setUsername(props.getProperty("username"));
            kv.setPassword(props.getProperty("password"));
            kv.setLongitude(props.getProperty("longitude"));
            kv.setLatitude(props.getProperty("latitude"));
            kv.setPosition(props.getProperty("position"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initial
        apis = new DailyApi();
        apis.setSwuIndex("http://authserverxg.swu.edu.cn/authserver/login?" +
                "service=https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay");
        apis.setSwuLogin("http://authserverxg.swu.edu.cn/authserver/login;sessionId" +
                "?service=https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay");
        apis.setGetMessage("https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/queryDailySginTasks");
        apis.setGetForm("https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/detailSignTaskInst");
        apis.setSubmitForm("https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/completeSignIn");

        // Get apis from server
        try {
            log.info("从服务器获取最新Api地址中...");
            String res = HttpRequest.get("https://api.neoniou.com/client/daily/getApis?username=" + kv.getUsername())
                    .execute().body();
            DailyApi resApi = JSONUtil.toBean(res, DailyApi.class);

            log.info("获取到Api地址，更新时间：[{}]", resApi.getUpdateTime());
            apis = resApi;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("未能从服务器获取到Api地址，将使用本地保存的地址");
        }

        return kv;
    }
}