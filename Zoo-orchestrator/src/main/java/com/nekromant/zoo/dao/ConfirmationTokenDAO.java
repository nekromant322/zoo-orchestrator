package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmationTokenDAO extends CrudRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findByToken(String token);
}
