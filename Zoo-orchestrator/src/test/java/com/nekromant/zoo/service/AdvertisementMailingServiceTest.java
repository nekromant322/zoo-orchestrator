package com.nekromant.zoo.service;

import dto.AdvertisementMailingMessageDTO;
import enums.MailingType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class AdvertisementMailingServiceTest {
    @InjectMocks
    AdvertisementMailingService mailingService;

    @Test
    public void sendMailing() {
        try {
            mailingService.sendMailing(new AdvertisementMailingMessageDTO("topic", "text", LocalDate.now(), MailingType.SMS));
            mailingService.sendMailing(new AdvertisementMailingMessageDTO("topic", "text", LocalDate.now(), MailingType.EMAIL));
        } catch (NullPointerException | IllegalArgumentException e) {
            Assert.fail("Формат MailingType");
        } catch (Exception ignored) {
            Assert.fail();
        }

        try {
            mailingService.sendMailing(new AdvertisementMailingMessageDTO("topic", "text", LocalDate.now(), null));
            Assert.fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {

        }
    }
}