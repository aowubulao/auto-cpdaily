package com.neoniou.daily.util;

import com.neoniou.com.mail.MailSmtp;
import com.neoniou.com.mail.NeoMail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
public class MailUtil {

    private static String username;
    private static String password;
    private static String notifyMail;

    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
        try {
            props.load(is);
            username = props.getProperty("username");
            password = props.getProperty("password");
            notifyMail = props.getProperty("notifyMail");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String subject, String text) {
        NeoMail neoMail = new NeoMail();
        try {
            neoMail.config(MailSmtp.QQ, username, password)
                    .subject(subject)
                    .from("auto-cp-daily")
                    .to(notifyMail)
                    .text(text)
                    .send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
