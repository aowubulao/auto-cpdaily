package daily.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Data
public class MessageBox {

    private Integer type;

    private String taskName;

    private String rateTaskBeginTime;

    private String rateTaskEndTime;

    private Date currentTime;

    private String signInstanceWid;

    private String signWid;
}
