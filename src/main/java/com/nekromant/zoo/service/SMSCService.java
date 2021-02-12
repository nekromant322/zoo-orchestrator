package com.nekromant.zoo.service;
/*
 * SMSC.RU API (smsc.ru) версия 1.3 (03.07.2019) smsc's sms sender package
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.*;
import java.lang.Math;

@Service
public class SMSCService {

    @Value("${SMSC.login}")
    String SMSC_LOGIN;

    @Value("${SMSC.password}")
    String SMSC_PASSWORD;

    @Value("${SMSC.https}")
    boolean SMSC_HTTPS;

    @Value("${SMSC.charset}")
    String SMSC_CHARSET;

    @Value("${SMSC.debug}")
    boolean SMSC_DEBUG;

    @Value("${SMSC.post}")
    boolean SMSC_POST;


    /**
     * constructors
     */
    public SMSCService() {
    }

    public SMSCService(String login, String password) {
        SMSC_LOGIN = login;
        SMSC_PASSWORD = password;
    }


    /**
     * Отправка SMS
     *
     * @param phones   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение
     * @return array (<id>, <количество sms>, <стоимость>, <баланс>) в случае успешной отправки
     * или массив (<id>, -<код ошибки>) в случае ошибки
     */
    public String[] send_sms(String phones, String message) {
        String[] m = {};

        try {
            m = _smsc_send_cmd("send", "cost=3&phones=" + URLEncoder.encode(phones, SMSC_CHARSET)
                    + "&mes=" + URLEncoder.encode(message, SMSC_CHARSET));
        } catch (UnsupportedEncodingException e) {

        }

        if (m.length > 1) {
            if (SMSC_DEBUG) {
                if (Integer.parseInt(m[1]) > 0) {
                    System.out.println("Сообщение отправлено успешно. ID: " + m[0] + ", всего SMS: " + m[1] + ", стоимость: " + m[2] + ", баланс: " + m[3]);
                } else {
                    System.out.print("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
                    System.out.println(Integer.parseInt(m[0]) > 0 ? (", ID: " + m[0]) : "");
                }
            }
        } else {
            System.out.println("Не получен ответ от сервера.");
        }

        return m;
    }

    ;

    /**
     * Формирование и отправка запроса
     *
     * @param cmd - требуемая команда
     * @param arg - дополнительные параметры
     */

    private String[] _smsc_send_cmd(String cmd, String arg) {
        /* String[] m = {}; */
        String ret = ",";

        try {
            String _url = (SMSC_HTTPS ? "https" : "http") + "://smsc.ru/sys/" + cmd + ".php?login=" + URLEncoder.encode(SMSC_LOGIN, SMSC_CHARSET)
                    + "&psw=" + URLEncoder.encode(SMSC_PASSWORD, SMSC_CHARSET)
                    + "&fmt=1&charset=" + SMSC_CHARSET + "&" + arg;

            String url = _url;
            int i = 0;
            do {
                if (i++ > 0) {
                    url = _url;
                    url = url.replace("://smsc.ru/", "://www" + (i) + ".smsc.ru/");
                }
                ret = _smsc_read_url(url);
            }
            while (ret == "" && i < 5);
        } catch (UnsupportedEncodingException e) {

        }

        return ret.split(",");
    }

    /**
     * Чтение URL
     *
     * @param url - ID cообщения
     * @return line - ответ сервера
     */
    private String _smsc_read_url(String url) {

        String line = "", real_url = url;
        String[] param = {};
        boolean is_post = (SMSC_POST || url.length() > 2000);

        if (is_post) {
            param = url.split("\\?", 2);
            real_url = param[0];
        }

        try {
            URL u = new URL(real_url);
            InputStream is;

            if (is_post) {
                URLConnection conn = u.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), SMSC_CHARSET);
                os.write(param[1]);
                os.flush();
                os.close();
                System.out.println("post");
                is = conn.getInputStream();
            } else {
                is = u.openStream();
            }

            InputStreamReader reader = new InputStreamReader(is, SMSC_CHARSET);

            int ch;
            while ((ch = reader.read()) != -1) {
                line += (char) ch;
            }

            reader.close();
        } catch (MalformedURLException e) { // Неверно урл, протокол...

        } catch (IOException e) {

        }

        return line;
    }



// Examples:
/*
		Smsc sd= new Smsc();
		// or
		Smsc sd= new Smsc("login", "password");

		sd.send_sms("79999999999", "Ваш пароль: 123", 1, "", "", 0, "", "");
		sd.get_sms_cost("79999999999", "Вы успешно зарегистрированы!", 0, 0, "", "");
		sd.get_status(sms_id, "79999999999");
		sd.get_balanse();
*/


}