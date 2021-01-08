package daily.constant;

/**
 * @author Neo.Zzj
 * @date 2020/7/20
 */
public interface CpDaily {

    String TENANT_ID = "1019318364515869";

    String CP_EXTENSION = "{\"systemName\":\"android\",\"systemVersion\":\"10\",\"model\":\"Mi 10\"" +
            ",\"deviceId\":\"r1\",\"appVersion\":" +
            "\"8.2.14\",\"lon\":r2,\"lat\":r3,\"userId\":\"r4\"}";

    String SIGN_INFO = "{\"longitude\":r1,\"latitude\":r2,\"isMalposition\":1,\"abnormalReason\":\"\"," +
            "\"signPhotoUrl\":\"\",\"isNeedExtra\":1,\"position\":\"local\"," +
            "\"uaIsCpadaily\":true,\"signInstanceWid\":\"siWid\",\"" +
            "extraFieldItems\":[{\"extraFieldItemValue\":\"正常，<37.2℃\",\"extraFieldItemWid\":itemId}]}";

    String SUBMIT_INFO = "{\"signInstanceWid\":\"siWid\",\"longitude\":r1,\"latitude\":r2,\"isMalposition\":0" +
            ",\"abnormalReason\":\"\",\"signPhotoUrl\":\"" +
            "https://wecres.cpdaily.com/counselor/1019318364515869/attachment/03b0532446d14be99f057ba744c7af95.png\",\"" +
            "position\":\"local\",\"qrUuid\":\"\"}";
}
