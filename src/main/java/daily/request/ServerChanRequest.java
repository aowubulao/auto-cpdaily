package daily.request;

import cn.hutool.http.HttpRequest;
import daily.AutoDailyCp;

/**
 * @author Neo.Zzj
 * @date 2020/12/29
 */
public class ServerChanRequest {

    private static final String URL;

    static {
        URL = "https://sc.ftqq.com/" + AutoDailyCp.info.getScKey() + ".send";
    }

    public static void sendMessage(String message, String description) {
        if (!"".equals(AutoDailyCp.info.getScKey())) {
            HttpRequest.post(URL)
                    .form("text", message)
                    .form("desp", description)
                    .execute();
        }
    }
}
