package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.MailingReceiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MailingReceiverDAO extends CrudRepository<MailingReceiver,Long> {

    Page<MailingReceiver> findAll(Pageable pageable);

    MailingReceiver getByEmail(String email);

    MailingReceiver getByPhoneNumber(String phone);

    @Modifying
    @Transactional
    @Query(value = "truncate table MAILING_RECEIVER", nativeQuery = true)
    void truncateTable();
}
