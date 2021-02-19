package daily.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Neo.Zzj
 * @date 2020/12/29
 */
@Slf4j
public class ServerChanRequest {

    private static String url = null;

    private static final String KEY = "SCU";

    private static String scKey;

    public static void initial(String key) {
        url = "https://sc.ftqq.com/" + key + ".send";
        scKey = key;
    }

    /**
     * ServerChan发送消息
     *
     * @param message 消息标题
     * @param description 消息描述
     */
    public static void sendMessage(String message, String description) {
        send(message, description);
    }

    private static void send(String message, String description) {
        if (scKey != null && scKey.contains(KEY)) {
            log.info("发送server酱通知消息至微信");

            try {
                HttpResponse response = HttpRequest.post(url)
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
