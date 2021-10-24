package com.nekromant.zoo.service;

import com.nekromant.zoo.client.NotificationZooClient;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.model.User;
import dto.MailingMessageDTO;
import dto.NotificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MailingService {
    @Autowired
    private NotificationZooClient notificationZooClient;
    @Autowired
    private UserDAO userDAO;

    @Value("${mailing.pagination.value}")
    private int paginationValue;

    @Value("${mailing.pagination.delay}")
    private int paginationDelay;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * Отправка рассылки {@link MailingMessageDTO} пользователям
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */
    public void sendMailing(MailingMessageDTO message) {
        switch (message.getType()) {
            case EMAIL:
                sendMailingByEmail(message);
                break;
            case TELEPHONE:
                sendMailingByPhone(message);
                break;
            default:
                log.warn("Что-то не так с MailingType (enum)");
        }
    }

    /**
     * Отправка рассылки {@link MailingMessageDTO} пользователям по телефону по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте и дате последней заявки после n
     */
    private void sendMailingByPhone(MailingMessageDTO message) {
        executorService.execute(() -> {
            List<String> sendUsersFailed = new ArrayList<>();
            int pageCount = 0;
            boolean emptyPage = false;
            while (!emptyPage) {
                Page<User> userRequestPage = userDAO.findAllByLastActionAfter(message.getDateFrom(),
                        PageRequest.of(pageCount, paginationValue));
                for (User user : userRequestPage.getContent()) {
                    String phone = user.getPhoneNumber();
                    try {
                        notificationZooClient.sendSms(new NotificationDTO(
                                phone,
                                message.getTopic(),
                                message.getText())
                        );
                        Thread.sleep(paginationDelay);
                    } catch (Exception e) {
                        log.warn("'{}' Не получил рассылку", phone);
                        sendUsersFailed.add(phone);
                    }
                }
                emptyPage = userRequestPage.isLast();
                pageCount++;
            }
            for (String phone : sendUsersFailed) {
                try {
                    notificationZooClient.sendEmail(new NotificationDTO(
                            phone,
                            message.getTopic(),
                            message.getText())
                    );
                    sendUsersFailed.remove(phone);
                    Thread.sleep(paginationDelay);
                } catch (Exception e) {
                    log.error("'{}' Не получил рассылку дважды ", phone);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Отправка рассылки {@link MailingMessageDTO} пользователям по почте по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте и дате последней заявки после n
     */
    private void sendMailingByEmail(MailingMessageDTO message) {
        executorService.execute(() -> {
            List<String> sendEmailUsersFailed = new ArrayList<>();
            int pageCount = 0;
            boolean emptyPage = false;
            while (!emptyPage) {
                Page<User> userRequestPage = userDAO.findAllByLastActionAfter(message.getDateFrom(),
                        PageRequest.of(pageCount, paginationValue));
                for (User user : userRequestPage.getContent()) {
                    String userEmail = user.getEmail();
                    try {
                        notificationZooClient.sendEmail(new NotificationDTO(
                                userEmail,
                                message.getTopic(),
                                message.getText())
                        );
                        Thread.sleep(paginationDelay);
                    } catch (Exception e) {
                        log.warn("'{}' Не получил рассылку", userEmail);
                        sendEmailUsersFailed.add(userEmail);
                    }
                }
                emptyPage = userRequestPage.isLast();
                pageCount++;
            }
            for (String email : sendEmailUsersFailed) {
                try {
                    notificationZooClient.sendEmail(new NotificationDTO(
                            email,
                            message.getTopic(),
                            message.getText())
                    );
                    sendEmailUsersFailed.remove(email);
                    Thread.sleep(paginationDelay);
                } catch (Exception e) {
                    log.error("'{}' Не получил рассылку дважды", email);
                    e.printStackTrace();
                }
            }
        });
    }
}
