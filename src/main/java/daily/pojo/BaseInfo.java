package daily.pojo;

import lombok.Data;

/**
 * @author Neo.Zzj
 * @date 2020/7/20
 */
@Data
public class BaseInfo {

    private String username;

    private String password;

    private String position;

    private String longitude;

    private String latitude;

    private String swuIndex;

    private String swuLogin;

    private String signGetMessage;

    private String signGetForm;

    private String signSubmitForm;

    private String attendanceGetMessage;

    private String attendanceGetForm;

    private String attendanceSubmitForm;

    private Boolean activeAttendance;

    private String apiVersion;

    private String scKey;

    public BaseInfo() {
    }

    public BaseInfo(String username, String password, String position, String longitude, String latitude, Boolean activeAttendance) {
        this.username = username;
        this.password = password;
        this.position = position;
        this.longitude = longitude;
        this.latitude = latitude;
        this.activeAttendance = activeAttendance;
    }
}
