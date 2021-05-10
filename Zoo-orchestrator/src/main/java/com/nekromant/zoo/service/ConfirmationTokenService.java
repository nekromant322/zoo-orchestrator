package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.ConfirmationTokenDAO;
import com.nekromant.zoo.model.ConfirmationToken;
import com.nekromant.zoo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenDAO confirmationTokenDAO;

    @Value("${app.const.tokenExpiredDelayInDays}")
    private String tokenExpiredDelay;

    /**
     * Генерация токена {@link ConfirmationToken} на основе мыла и номера телефона
     * По итогу создается токен на основе HashMap с двумя ключами
     *
     * @param email - email {@link User}
     * @param phone - phone {@link User}
     * @return - encrypted string (token)
     */
    public String getEncodedToken(String email, String phone) {

        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("phone", phone);

        return Base64.getEncoder().encodeToString(map.toString().getBytes(StandardCharsets.UTF_8));
    }

    public ConfirmationToken getToken(String token) {
        return confirmationTokenDAO.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not found!"));
    }

    public void addToken(String token, String email) {
        confirmationTokenDAO.save(new ConfirmationToken(token, email, LocalDate.now().plusDays(Long.parseLong(tokenExpiredDelay))));
    }

    public void deleteToken(ConfirmationToken confirmationToken) {
        confirmationTokenDAO.delete(confirmationToken);
    }

}
