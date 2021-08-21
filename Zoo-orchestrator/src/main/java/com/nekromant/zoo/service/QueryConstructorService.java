package com.nekromant.zoo.service;

import com.nekromant.zoo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
public class QueryConstructorService {

    @Value("${server.address}")
    private String address;

    @Value("${server.port}")
    private String port;

    private final String CONFIRM_REGISTRATION_PATH = "/confirmReg";

    /**
     * Вспомогательный метод генерации ссылки для изменения пароля юзера {@link User}
     * отправляется по email юзеру
     *
     * @param token - токен
     * @return - url
     */
    public UriComponents buildConfirmationUrlWithToken(String token) {

        return UriComponentsBuilder.newInstance()
                .scheme("http").host(address).port(port)
                .path(CONFIRM_REGISTRATION_PATH).query("key={keyword}").buildAndExpand(token)
                .encode(StandardCharsets.UTF_8);
    }
}
