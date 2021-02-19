package daily.constant;

/**
 * @author Neo.Zzj
 * @date 2021/2/19
 */
public interface CpDailyApi {

    String SWU_INDEX = "http://authserverxg.swu.edu.cn/authserver/login?service=https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay";

    String SWU_LOGIN = "http://authserverxg.swu.edu.cn/authserver/login;sessionId?service=https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay";

    String SIGN_GET_MESSAGE = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay";

    String SIGN_GET_FORM = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/detailSignInstance";

    String SIGN_SUBMIT_FORM = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/submitSign";

    String ATTENDANCE_GET_MESSAGE = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/getStuAttendacesInOneDay";

    String ATTENDANCE_GET_FORM = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/detailSignInstance";

    String ATTENDANCE_SUBMIT_FORM = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/submitSign";
}
