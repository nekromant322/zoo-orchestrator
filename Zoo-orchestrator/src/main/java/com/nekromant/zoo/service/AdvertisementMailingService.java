package com.nekromant.zoo.service;

import com.nekromant.zoo.client.NotificationZooClient;
import com.nekromant.zoo.dao.MailingReceiverDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.MailingReceiver;
import dto.AdvertisementMailingMessageDTO;
import dto.NotificationDTO;
import enums.MailingType;
import lombok.SneakyThrows;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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

    @Value("${mailing.pagination.threads}")
    private int paginationThreads;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);


    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */
    public void sendMailing(AdvertisementMailingMessageDTO message) {
        insertInQueue(message);
    }


    /**
     * Вставка в очередь рассылки {@link AdvertisementMailingMessageDTO} пользователям по типу
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */
    private void insertInQueue(AdvertisementMailingMessageDTO message) {
        switch (message.getType()) {
            case EMAIL:
                executorService.execute(() -> sendMailingByEmail(message));
                break;
            case SMS:
                executorService.execute(() -> sendMailingByPhone(message));
                break;
            default:
                throw new IllegalArgumentException("Что-то не так с MailingType (enum)");
        }
    }

    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям {@link MailingReceiver} по почте по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */

    private void sendMailingByEmail(AdvertisementMailingMessageDTO message) {
        updateUniqMailingReceivers(message);
        List<MailingReceiver> sendUsersFailed = new ArrayList<>();
        int pageCount = (int) Math.ceil(mailingReceiverDAO.count() / paginationValue);
        Stream
                .iterate(pageCount, n -> n - 1)
                .limit(pageCount + 1)
                .map(el -> mailingReceiverDAO.getAllByType(MailingType.EMAIL, PageRequest.of(el, paginationValue)))
                .forEach(el -> {
                    for (MailingReceiver receiver : el.getContent()) {
                        String email = receiver.getEmail();
                        if (email != null && !email.isEmpty()) {
                            try {
                                NotificationDTO notification = new NotificationDTO(email, receiver.getTopic(), receiver.getText());
                                notificationZooClient.sendEmail(notification);
                                mailingReceiverDAO.delete(receiver);
                            } catch (Exception e) {
                                log.warn("'{}' Не получил рассылку", email);
                                sendUsersFailed.add(receiver);
                            }
                        }
                    }
                    try {
                        Thread.sleep(paginationDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        sendMailingForListReceivers(sendUsersFailed, message);
    }

    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям {@link MailingReceiver} по телефону по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     * Если пользователю не пришла рассылка в первый раз он помещается в список, после всех пользователей
     * отправляется еще раз рассылка по этому списку
     *
     * @param message сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     */
    private void sendMailingByPhone(AdvertisementMailingMessageDTO message) {
        updateUniqMailingReceivers(message);
        List<MailingReceiver> sendUsersFailed = new ArrayList<>();
        int pageCount = (int) Math.ceil(mailingReceiverDAO.count() / paginationValue);
        Stream
                .iterate(pageCount, n -> n - 1)
                .limit(pageCount + 1)
                .map(el -> mailingReceiverDAO.getAllByType(MailingType.SMS, PageRequest.of(el, paginationValue)))
                .forEach(el -> {
                    for (MailingReceiver receiver : el.getContent()) {
                        String phone = receiver.getPhoneNumber();
                        if (phone != null && !phone.isEmpty()) {
                            try {
                                NotificationDTO notification = new NotificationDTO(phone, receiver.getTopic(), receiver.getText());
                                notificationZooClient.sendSms(notification);
                                mailingReceiverDAO.delete(receiver);
                            } catch (Exception e) {
                                log.warn("'{}' Не получил рассылку", phone);
                                sendUsersFailed.add(receiver);
                            }
                        }
                    }
                    try {
                        Thread.sleep(paginationDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        sendMailingForListReceivers(sendUsersFailed, message);
    }

    /**
     * Отправка рассылки {@link AdvertisementMailingMessageDTO} пользователям {@link MailingReceiver} по частям
     * Задержку и кол-во пользователей на одной итерации можно настроить в конфиге приложения
     *
     * @param message   сообщение с информацией о теме, тексте, типе и дате последней заявки после n
     * @param receivers лист пользователей для отправки
     */
    private void sendMailingForListReceivers(List<MailingReceiver> receivers, AdvertisementMailingMessageDTO message) {
        for (MailingReceiver receiver : receivers) {
            try {
                switch (message.getType()) {
                    case EMAIL:
                        notificationZooClient.sendEmail(new NotificationDTO(receiver.getEmail(), receiver.getTopic(), receiver.getText()));
                        break;
                    case SMS:
                        notificationZooClient.sendSms(new NotificationDTO(receiver.getPhoneNumber(), receiver.getTopic(), receiver.getText()));
                        break;
                    default:
                        throw new IllegalArgumentException("Что-то не так с MailingType (enum)");
                }
                mailingReceiverDAO.delete(receiver);
                Thread.sleep(paginationDelay);
            } catch (Exception e) {
                log.error("'{}' Не получил рассылку из листа", receiver.getEmail());
                e.printStackTrace();
            }
        }
    }

    /**
     * Обновление {@link MailingReceiver} пользователей в таблице, для последующей рассылки
     *
     * @param message сообщение с информацией о теме, тексте и дате последней заявки после n
     */
    private void updateUniqMailingReceivers(AdvertisementMailingMessageDTO message) {
        int pageCount = 0;
        boolean emptyPage = false;
        List<AnimalRequest> animalRequestPage;
        while (!emptyPage) {
            animalRequestPage = animalRequestService.getPaginationRequestsWithLastDateAfter(
                    message.getDateFrom(),
                    PageRequest.of(pageCount, 20)
            );
            if (message.getType() == MailingType.SMS) {
                for (AnimalRequest animalRequest : animalRequestPage) {
                    List<MailingReceiver> receivers = mailingReceiverDAO.getAllByPhoneNumber(animalRequest.getPhoneNumber());
                    if (receivers.stream().noneMatch(el -> el.getText().equals(message.getText()))) {
                        mailingReceiverDAO.save(new MailingReceiver(animalRequest.getPhoneNumber(), null, message.getTopic(), message.getText(), message.getType()));
                    }
                }
            }
            if (message.getType() == MailingType.EMAIL) {
                for (AnimalRequest animalRequest : animalRequestPage) {
                    List<MailingReceiver> receivers = mailingReceiverDAO.getAllByEmail(animalRequest.getEmail());
                    if (receivers.stream().noneMatch(el -> el.getText().equals(message.getText()))) {
                        mailingReceiverDAO.save(new MailingReceiver(null, animalRequest.getEmail(), message.getTopic(), message.getText(), message.getType()));
                    }
                }
            }
            emptyPage = animalRequestPage.size() < paginationValue;
            pageCount++;
        }
    }
}
