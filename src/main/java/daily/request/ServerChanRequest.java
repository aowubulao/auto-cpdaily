package daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import daily.AutoDailyCp;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Neo.Zzj
 * @date 2020/12/29
 */
@Slf4j
public class ServerChanRequest {

    private static final String URL;

    private static final String KEY = "SCU";

    static {
        URL = "https://sc.ftqq.com/" + AutoDailyCp.info.getScKey() + ".send";
    }

    public static void sendMessage(String message, String description) {
        String scKey = AutoDailyCp.info.getScKey();
        if (scKey != null && scKey.contains(KEY)) {
            log.info("发送server酱通知消息至微信");

            try {
                HttpResponse response = HttpRequest.post(URL)
                        .form("text", message)
                        .form("desp", description)
                        .execute();

                if (response.isOk()) {
                    log.info("server酱推送成功！");
                } else {
                    log.info("server酱推送失败，返回状态码: [{}]", response.getStatus());
                }
            } catch (Exception e) {
                log.error("server酱通知消息发送失败，错误原因：", e);
            }
        }
    }
}
