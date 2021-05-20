package daily.constant;

/**
 * @author Neo.Zzj
 * @date 2021/2/19
 */
public interface CpDailyApi {

    String SWU_LT = "https://swu.campusphere.net/iap/security/lt";

    String SWU_DO_LOGIN = "https://swu.campusphere.net/iap/doLogin";

    String SWU_AUTH_LOGIN = "https://swu.campusphere.net/iap/login?service=https%3A%2F%2Fswu.campusphere.net%2Fportal%2Flogin";

    String SIGN_GET_MESSAGE = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/getStuSignInfosInOneDay";

    String SIGN_GET_FORM = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/detailSignInstance";

    String SIGN_SUBMIT_FORM = "https://swu.campusphere.net/wec-counselor-sign-apps/stu/sign/submitSign";

    String ATTENDANCE_GET_MESSAGE = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/getStuAttendacesInOneDay";

    String ATTENDANCE_GET_FORM = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/detailSignInstance";

    String ATTENDANCE_SUBMIT_FORM = "https://swu.campusphere.net/wec-counselor-attendance-apps/student/attendance/submitSign";
}
