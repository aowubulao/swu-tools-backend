package com.neoniou.swu.util;

import io.github.biezhi.ome.OhMyEmail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Neo.Zzj
 */
public class MailUtil {

    private static String username;
    private static String password;
    private static String to;

    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ome.properties");
        try {
            props.load(is);
            username = props.getProperty("username");
            password = props.getProperty("password");
            to = props.getProperty("to");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendPromptMail(String subject, String text) {
        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), username, password);
        try {
            OhMyEmail.subject(subject)
                    .from("Web Notify")
                    .to(to)
                    .text(text)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
