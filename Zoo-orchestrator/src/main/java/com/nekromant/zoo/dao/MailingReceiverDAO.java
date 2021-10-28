package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.MailingReceiver;
import enums.MailingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MailingReceiverDAO extends CrudRepository<MailingReceiver,Long> {
    Page<MailingReceiver> getAllByType(MailingType type, Pageable pageable);
}
