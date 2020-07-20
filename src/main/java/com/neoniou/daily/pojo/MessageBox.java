package com.neoniou.daily.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Data
public class MessageBox {

    private String taskName;

    private String rateTaskBeginTime;

    private String rateTaskEndTime;

    private Date currentTime;

    private String signInstanceWid;

    private String signWid;
}
