package com.nekromant.zoo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSender {

    @Value("${emailLogin}")
    private String emailLogin;

    @Value("${emailPassword}")
    private String emailPassword;

    public MailSender() {
    }

    public MailSender(String emailLog, String emailPass) {
        emailLogin = emailLog;
       emailPassword = emailPass;
    }

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("port")));

        mailSender.setUsername(emailLogin);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol",env.getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth",env.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug",env.getProperty("mail.debug"));

        return mailSender;
    }
}
