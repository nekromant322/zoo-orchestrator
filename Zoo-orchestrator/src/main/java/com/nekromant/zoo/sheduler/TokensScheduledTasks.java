package com.nekromant.zoo.sheduler;

import com.nekromant.zoo.dao.ConfirmationTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TokensScheduledTasks {

    @Autowired
    private ConfirmationTokenDAO confirmationTokenDAO;

    /**
     * Job по удалению токенов с истекшим сроков действия
     */
    @Scheduled(fixedRateString = "${app.const.tokens-remove-scheduler-delay}")
    private void clearExpiredTokens() {
        confirmationTokenDAO.findAll().forEach(item -> {
            if (item.getExpiredDate().isBefore(LocalDate.now()))
                confirmationTokenDAO.delete(item);
        });
    }
}
