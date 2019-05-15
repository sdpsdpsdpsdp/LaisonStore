package com.laisontech.laisondownloader.utils;

import android.os.AsyncTask;

import com.laisontech.laisondownloader.http.HttpConst;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * Created by lu on 2017/7/18.
 * 邮箱验证码功能
 * 这边需要导入比较多的架包
 */
public class EmailSend {
    //将发送邮件验证码的方法写成静态，方便调用
    //这边将发件人邮箱固定在成公司邮箱了（不管什么操作，都使用公司邮箱），别名传进来取
    /**
     * sendName 发送人显示别名（邮件点进去之前显示是谁发送的）
     *  toEmail 收件人邮箱
     * subject 邮件主题
     * msg 邮件详情
     * @return true 发送是否成功
     * */
    public static boolean SendEmailCode(final String toEmail,final String msgCode){
        // 建立发送邮件任务
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    sendEmailByApacheCommonsEmail(toEmail, msgCode);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }.execute();
        return true;
    }
    private static void sendEmailByApacheCommonsEmail(String toEmail,String msgCode)
            throws EmailException {
        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setDebug(true);
        //这里使用163邮箱服务器，实际需要修改为对应邮箱服务器
        //smtp.163.com:25   smtp.qq.com:587
        email.setHostName(HttpConst.EMAIL_HOST);
        email.setSmtpPort(HttpConst.EMAIL_PORT);//一般QQ邮箱578或者465、163邮箱25
        email.setSocketTimeout(HttpConst.EMAIL_TAIMEOUT);//设置超时时间
        email.setCharset(HttpConst.EMAIL_CHAR);//设置字符编码
        email.setTLS(true);
        email.setStartTLSEnabled(true);
        email.setSSL(true);
        //email.setAuthentication("289112583@qq.com", "vikqrvbscsmybhie");
        email.setAuthentication(HttpConst.EMAIL_ACCOUNT, HttpConst.EMAIL_PWD);//设置发送者邮箱密码
        email.addTo(toEmail, toEmail);//设置接收这邮箱和名称
        //email.addBcc(bcc);
        //email.addCc(cc);
        email.setFrom(HttpConst.EMAIL_ACCOUNT, HttpConst.EMAIL_NAME);//设置来自邮箱和主题名
        email.setSubject(HttpConst.EMAIL_SUBJECT);
        email.setMsg(msgCode);//设置内容
        String sendStr = email.send();//执行发送
        System.out.println("sendStr=" + sendStr);
    }
}
