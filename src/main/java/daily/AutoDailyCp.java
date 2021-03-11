package daily;

import daily.pojo.KeyValueClass;

/**
 * @author Neo.Zzj
 * @date 2020/6/22
 */
public class AutoDailyCp {

    /**
     * Github Action以及测试用入口
     *
     * @param args Secret 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        SignApplication.start(args);
    }

    /**
     * 腾讯云函数入口
     *
     * @param kv 腾讯云函数必须使用的一个类，无实际作用
     * @throws Exception 异常
     */
    public void mainHandler(KeyValueClass kv) throws Exception {
        SignApplication.start();
    }
}