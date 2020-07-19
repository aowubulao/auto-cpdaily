package com.neoniou.daily.pojo;

import lombok.Data;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
@Data
public class MessageBox {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息链接，用于获取 signWid 和 signInstanceWid
     */
    private String mobileUrl;

    /**
     * 是否提交
     */
    private Boolean isHandled;
}
