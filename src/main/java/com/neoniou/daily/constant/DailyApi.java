package com.neoniou.daily.constant;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
public interface DailyApi {

    String SWU_INDEX = "http://authserverxg.swu.edu.cn/authserver/" +
            "login?service=http%3A%2F%2Fauthserverxg.swu.edu.cn%" +
            "2Fauthserver%2Fmobile%2Fcallback%3FappId%3D177043231&login_type=mobileLogin";

    String SWU_LOGIN = "http://authserverxg.swu.edu.cn/authserver/login;" +
            "replace" +
            "service=http%3A%2F%2Fauthserverxg.swu.edu.cn%2Fauthserver%2Fmobile%2Fcallback%3FappId%3D177043231&login_type=mobileLogin";

    String GET_MESSAGE = "https://openapi.cpdaily.com/message_pocket_web/V2/mp/restful/mobile/message/extend/get";

    String GET_FORM = "https://swu.cpdaily.com/wec-counselor-sign-apps/stu/sign/detailSignInstance";

    String SUBMIT_FORM = "https://swu.cpdaily.com/wec-counselor-sign-apps/stu/sign/submitSign";
}
