package com.nekromant.zoo.service;

import com.nekromant.zoo.client.NotificationZooClient;
import com.nekromant.zoo.dao.MailingReceiverDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.MailingReceiver;
import dto.AdvertisementMailingMessageDTO;
import dto.NotificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AdvertisementMailingService {
    @Autowired
    private NotificationZooClient notificationZooClient;

    @Autowired
    private AnimalRequestService animalRequestService;

    @Autowired
    private MailingReceiverDAO mailingReceiverDAO;

    @Value("${mailing.pagination.value}")
    private int paginationValue;

    @Value("${mailing.pagination.delay}")
    private int paginationDelay;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */
    public void sendMailing(AdvertisementMailingMessageDTO message) {
        switch (message.getType()) {
            case EMAIL:
                sendMailingByEmail(message);
                break;
            case TELEPHONE:
                sendMailingByPhone(message);
                break;
            default:
                log.warn("Что-то не так с MailingType (enum)");
                throw new IllegalArgumentException("Что-то не так с MailingType (enum)");
        }
    }

    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям {@link MailingReceiver} по телефону по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте и дате последней заявки после n
     */
    private void sendMailingByPhone(AdvertisementMailingMessageDTO message) {
        executorService.execute(() -> {
            updateUniqMailingReceivers(message.getDateFrom());
            List<String> sendUsersFailed = new ArrayList<>();
            int pageCount = 0;
            boolean emptyPage = false;
            while (!emptyPage) {
                Page<MailingReceiver> mailingReceivers = mailingReceiverDAO.findAll(PageRequest.of(pageCount, paginationValue));
                for (MailingReceiver receiver : mailingReceivers.getContent()) {
                    String phone = receiver.getPhoneNumber();
                    if (phone != null && !phone.isEmpty()) {
                        try {
                            NotificationDTO notification = new NotificationDTO(phone, message.getTopic(), message.getText());
                            notificationZooClient.sendSms(notification);
                        } catch (Exception e) {
                            log.warn("'{}' Не получил рассылку", phone);
                            sendUsersFailed.add(phone);
                        }
                    }
                }
                try {
                    Thread.sleep(paginationDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emptyPage = mailingReceivers.isLast();
                pageCount++;
            }
            for (String phone : sendUsersFailed) {
                try {
                    notificationZooClient.sendSms(new NotificationDTO(
                            phone,
                            message.getTopic(),
                            message.getText())
                    );
                    Thread.sleep(paginationDelay);
                } catch (Exception e) {
                    log.error("'{}' Не получил рассылку дважды ", phone);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям {@link MailingReceiver} по почте по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте и дате последней заявки после n
     */
    private void sendMailingByEmail(AdvertisementMailingMessageDTO message) {
        executorService.execute(() -> {
            updateUniqMailingReceivers(message.getDateFrom());
            List<String> sendEmailUsersFailed = new ArrayList<>();
            int pageCount = 0;
            boolean emptyPage = false;
            while (!emptyPage) {
                Page<MailingReceiver> mailingReceivers = mailingReceiverDAO.findAll(PageRequest.of(pageCount, paginationValue));
                for (MailingReceiver receiver : mailingReceivers.getContent()) {
                    String userEmail = receiver.getEmail();
                    if (userEmail != null && !userEmail.isEmpty())
                        try {
                            notificationZooClient.sendEmail(new NotificationDTO(
                                    userEmail,
                                    message.getTopic(),
                                    message.getText())
                            );
                        } catch (Exception e) {
                            log.warn("'{}' Не получил рассылку", userEmail);
                            sendEmailUsersFailed.add(userEmail);
                        }
                }
                try {
                    Thread.sleep(paginationDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emptyPage = mailingReceivers.isLast();
                pageCount++;
            }
            for (String email : sendEmailUsersFailed) {
                try {
                    notificationZooClient.sendEmail(new NotificationDTO(
                            email,
                            message.getTopic(),
                            message.getText())
                    );
                    Thread.sleep(paginationDelay);
                } catch (Exception e) {
                    log.error("'{}' Не получил рассылку дважды", email);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Обновление {@link MailingReceiver} пользователей в таблице, для последующей рассылки
     *
     * @param date дата, с которой последней заявки после n добавлять пользователей в таблицу
     */
    private void updateUniqMailingReceivers(LocalDate date) {
        mailingReceiverDAO.truncateTable();
        int pageCount = 0;
        boolean emptyPage = false;
        List<AnimalRequest> animalRequestPage;
        while (!emptyPage) {
            animalRequestPage = animalRequestService.getPaginationRequestsWithLastDateAfter(date, PageRequest.of(pageCount, 20));
            for (AnimalRequest animalRequest : animalRequestPage) {
                String bufEmail = animalRequest.getEmail();
                String bufPhone = animalRequest.getPhoneNumber();
                String mail = mailingReceiverDAO.getByEmail(bufEmail) == null ? bufEmail : null;
                String phone = mailingReceiverDAO.getByPhoneNumber(bufPhone) == null ? bufPhone : null;
                mailingReceiverDAO.save(new MailingReceiver(phone, mail));
            }
            emptyPage = animalRequestPage.size() < paginationValue;
            pageCount++;
        }
    }

    public void setPaginationDelay(int val) {
        paginationDelay = val;
    }

    public void setPaginationValue(int val) {
        paginationValue = val;
    }
}
