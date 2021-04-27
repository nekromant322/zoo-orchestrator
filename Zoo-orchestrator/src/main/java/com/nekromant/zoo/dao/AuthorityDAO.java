package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityDAO extends CrudRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);
}
