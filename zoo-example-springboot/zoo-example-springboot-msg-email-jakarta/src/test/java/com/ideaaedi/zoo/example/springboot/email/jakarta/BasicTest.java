package com.ideaaedi.zoo.example.springboot.email.jakarta;

import com.google.common.collect.Lists;
import com.ideaaedi.commonds.io.IOUtil;
import com.ideaaedi.commonds.path.PathUtil;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.MailFaceUtil;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.core.MailSender;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.core.MailSenderManager;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.entity.MailFile;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.entity.MailSendInfo;
import com.ideaaedi.zoo.diy.feature.msg.api.mail.entity.MailSendResult;
import jakarta.activation.MimetypesFileTypeMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 基础测试
 */
@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootMsgEmailJakartaApplication.class
)
public class BasicTest {
    
    String testResourcesDir = PathUtil.getProjectRootDir(BasicTest.class)
            .replace("/target/classes/",
            "/src/main/resources/");
        
    @Test
    @SneakyThrows
    public void test0() {
        // 通过zoo.msg.sms.default-sender-id=SmsSenderIdA指定了默认的短信发送器
        MailSender defaultSender = MailFaceUtil.getDefaultSender();
        Assertions.assertNotNull(defaultSender);
        Assertions.assertEquals("xyz", defaultSender.id());
    
    
        String senderIdXyz = "xyz";
        String senderIdQwer = "qwer";
        MailSenderManager senderManager = MailFaceUtil.getSenderManager();
        MailSender senderXyz = senderManager.find(senderIdXyz);
        MailSender senderQwer = senderManager.find(senderIdQwer);
        Assertions.assertNotNull(senderXyz);
        Assertions.assertNotNull(senderQwer);
        TimeUnit.MILLISECONDS.sleep(20);
        System.err.println("              >>> 开始使用邮件门面类MailFaceUtil（或MailSender）发送邮件吧！ <<< ");
    }
    
    /**
     * 使用默认邮件发送器发送
     */
    @Test
    @SneakyThrows
    public void test1() {
        // 不指定邮件发送器（通过senderId参数），则走zoo.msg.sms.default-sender-id指定的默认邮件发送器
        MailSendResult mailSendResult = MailFaceUtil.sendText(
                Lists.newArrayList("shuaige@demo.net"),
                "嘿嘿嘿",
                "今晚吃串串不咯？"
        );
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送普通邮件
     */
    @Test
    @SneakyThrows
    public void test2() {
        String senderIdXyz = "xyz";
        //String senderIdQwer = "qwer";
        MailSendResult mailSendResult = MailFaceUtil.sendText(
                senderIdXyz,
                Lists.newArrayList("your163address@163.com", "shuaige@demo.net"),
                "sendText 测试邮件subjecy",
                "sendText 测试邮件内容context"
        );
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送普通邮件 + 附件
     */
    @Test
    @SneakyThrows
    public void test3() {
        //String senderIdXyz = "xyz"; // xyz开启了debug
        String senderIdQwer = "qwer"; // qwer未开启debug
        MailSendResult mailSendResult = MailFaceUtil.sendText(
                senderIdQwer,
                Lists.newArrayList("your163address@163.com", "shuaige@demo.net"),
                "sendText 测试邮件subjecy 带附件",
                "sendText 测试邮件内容context 带附件",
                MailFile.of(new File(testResourcesDir, "a.txt")),
                MailFile.of(new File(testResourcesDir, "b.pptx")),
                MailFile.of(new File(testResourcesDir, "c.xlsx"))
        );
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送html邮件
     */
    @Test
    @SneakyThrows
    public void test4() {
        MailSendResult mailSendResult = MailFaceUtil.sendHtml(
                Lists.newArrayList("shuaige@demo.net"),
                "sendHtml 测试邮件subjecy",
                """
                        <!DOCTYPE html>
                        <html lang="zh-CN">
                        <head>
                            <meta charset="UTF-8">
                            <title>简单页面</title>
                        </head>
                        <body>
                            <h1>欢迎访问我的网站</h1>
                            <p>请欣赏风景：</p>
                            <img src="https://img0.baidu.com/it/u=1388364636,3796712962&fm=253&fmt=auto&app=120&f=JPEG?w=475&h=677" alt="示例图片" />
                        </body>
                        </html>
                        """
        );
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送html邮件 + 附件
     */
    @Test
    @SneakyThrows
    public void test5() {
        //String senderIdXyz = "xyz"; // xyz开启了debug
        String senderIdQwer = "qwer"; // qwer未开启debug
        MailSendResult mailSendResult = MailFaceUtil.sendHtml(
                senderIdQwer,
                Lists.newArrayList("shuaige@demo.net"),
                "sendHtml 测试邮件subjecy 带附件",
                """
                        <!DOCTYPE html>
                        <html lang="zh-CN">
                        <head>
                            <meta charset="UTF-8">
                            <title>简单页面</title>
                        </head>
                        <body>
                            <h1>欢迎访问我的网站</h1>
                            <p>请欣赏风景：</p>
                            <img src="https://img0.baidu.com/it/u=1388364636,3796712962&fm=253&fmt=auto&app=120&f=JPEG?w=475&h=677" alt="示例图片" />
                        </body>
                        </html>
                        """,
                MailFile.of(new File(testResourcesDir, "a.txt")),
                MailFile.of(new File(testResourcesDir, "b.pptx")),
                MailFile.of(new File(testResourcesDir, "c.xlsx"))
        );
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送更复杂的邮件
     */
    @Test
    @SneakyThrows
    public void test6() {
        String uid1 = "uid1";
        String uid2 = "uid2";
        MailSendInfo mailSendInfo = MailSendInfo.builder()
                .to(Lists.newArrayList("shuaige@demo.net"))
                .subject("sendHtml 测试邮件subjecy 复杂邮件")
                .content(
                        String.format(
                        """
                                <!DOCTYPE html>
                                <html lang="zh-CN">
                                <head>
                                    <meta charset="UTF-8">
                                    <title>简单页面</title>
                                </head>
                                <body>
                                    <h1>欢迎访问我的网站</h1>
                                    <p>请欣赏风景1：</p>
                                    <img src="https://img0.baidu.com/it/u=1388364636,3796712962&fm=253&fmt=auto&app=120&f=JPEG?w=475&h=677" alt="示例图片" />
                                    <p>请欣赏风景2：</p>
                                    <img src="cid:%s">
                                    <p>请欣赏风景3：</p>
                                    <img src="cid:%s">
                                </body>
                                </html>
                                """,
                                uid1,
                                uid2
                        )
                )
                .isHtml(true)
                // 内嵌资源（嵌到content的哪里，取决于对应占位符的位置）
                .inlines(
                        Lists.newArrayList(
                                MailFile.of(new File(testResourcesDir, "1.jpeg")).uid(uid1),
                                MailFile.of(new File(testResourcesDir, "2.jpeg")).uid(uid2)
                        )
                )
                .attachments(
                        Lists.newArrayList(
                                MailFile.of(new File(testResourcesDir, "a.txt")),
                                MailFile.of(new File(testResourcesDir, "b.pptx")),
                                MailFile.of(new File(testResourcesDir, "c.xlsx"))
                        )
                )
                .cc(
                        Lists.newArrayList(
                                "亨得帅<your163address1@163.com>"
                        )
                )
                .bcc(
                        Lists.newArrayList(
                                "邓二洋<your163address2@163.com>"
                        )
                )
                .build();
        System.err.println("发送的内容是：\n" + mailSendInfo.getContent());
        MailSendResult mailSendResult = MailFaceUtil.send(mailSendInfo); // 不指定邮件发送器，则走zoo.msg.sms.default-sender-id指定的默认邮件发送器
        System.err.println(mailSendResult);
    }
    
    /**
     * 发送更复杂的邮件 - 用bytes file
     */
    @Test
    @SneakyThrows
    public void test7() {
        String uid1 = "uid1";
        String uid2 = "uid2";
        MailSendInfo mailSendInfo = MailSendInfo.builder()
                .to(Lists.newArrayList("小可爱<2316232617@qq.com>"))
                .subject("sendHtml 测试邮件subjecy 复杂邮件 - 用bytes file")
                .content(
                        String.format(
                        """
                                <!DOCTYPE html>
                                <html lang="zh-CN">
                                <head>
                                    <meta charset="UTF-8">
                                    <title>简单页面</title>
                                </head>
                                <body>
                                    <h1>欢迎访问我的网站</h1>
                                    <p>请欣赏风景1：</p>
                                    <img src="https://img0.baidu.com/it/u=1388364636,3796712962&fm=253&fmt=auto&app=120&f=JPEG?w=475&h=677" alt="示例图片" />
                                    <p>请欣赏风景2：</p>
                                    <img src="cid:%s">
                                    <p>请欣赏风景3：</p>
                                    <img src="cid:%s">
                                </body>
                                </html>
                                """,
                                uid1,
                                uid2
                        )
                )
                .isHtml(true)
                // 内嵌资源（嵌到content的哪里，取决于对应占位符的位置）
                .inlines(
                        Lists.newArrayList(
                                MailFile.of(IOUtil.toBytes(new File(testResourcesDir, "1.jpeg")))
                                        .uid(uid1).contentType("image/jpeg"),
                                MailFile.of(IOUtil.toBytes(new File(testResourcesDir, "2.jpeg")))
                                        .uid(uid2).contentType("image/jpeg")
                        )
                )
                .attachments(
                        Lists.newArrayList(
                                MailFile.of(IOUtil.toBytes(new File(testResourcesDir, "a.txt")))
                                        .contentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(".txt"))
                                        .filename("附件A.txt"),
                                MailFile.of(IOUtil.toBytes(new File(testResourcesDir, "b.pptx")))
                                        .contentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(".pptx"))
                                        .filename("附件B.pptx"),
                                MailFile.of(IOUtil.toBytes(new File(testResourcesDir, "c.xlsx")))
                                        .contentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(".xlsx"))
                                        .filename("附件C.xlsx")
                        )
                )
                .cc(
                        Lists.newArrayList(
                                "亨得帅<your3@163.com>"
                        )
                )
                .bcc(
                        Lists.newArrayList(
                                "邓三洋<your4@163.com>"
                        )
                )
                .build();
        
        System.err.println("发送的内容是：\n" + mailSendInfo.getContent());
        MailSendResult mailSendResult = MailFaceUtil.send(mailSendInfo); // 不指定邮件发送器，则走zoo.msg.sms.default-sender-id指定的默认邮件发送器
        System.err.println(mailSendResult);
    }
}