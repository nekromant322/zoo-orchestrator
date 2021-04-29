package com.nekromant.zoo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {

    @Value("${spring.security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${spring.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${spring.security.jwt.key-alias}")
    private String keyAlias;

    @Value("${spring.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;

    @SneakyThrows
    public String generateToken(String login, String authorities) {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", login);
        claims.put("authorities", authorities);
        return Jwts.builder()
                .setSubject(login)
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, keyStore().getKey(keyAlias, keyStorePassword.toCharArray()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(keyStore().getKey(keyAlias, keyStorePassword.toCharArray())).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("invalid token");
        }
        return false;
    }

    @SneakyThrows
    public String getLoginFromToken(String token) {
        return (String) Jwts.parser().setSigningKey(keyStore()
                .getKey(keyAlias, keyStorePassword.toCharArray()))
                .parseClaimsJws(token)
                .getBody()
                .get("username");
    }

    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Unable to load keystore: {}", keyStorePath, e);
        }

        throw new IllegalArgumentException("Unable to load keystore");
    }
}