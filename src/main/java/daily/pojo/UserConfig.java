package daily.pojo;

import lombok.Data;

/**
 * @author Neo.Zzj
 * @date 2020/7/20
 */
@Data
public class UserConfig {

    private String username;

    private String password;

    private String position;

    private String longitude;

    private String latitude;

    private Boolean activeAttendance;

    private String scKey;

    public UserConfig() {
        this.username = null;
    }

    public UserConfig(String username, String password, String position, String longitude, String latitude, Boolean activeAttendance) {
        this.username = username;
        this.password = password;
        this.position = position;
        this.longitude = longitude;
        this.latitude = latitude;
        this.activeAttendance = activeAttendance;
    }
}
