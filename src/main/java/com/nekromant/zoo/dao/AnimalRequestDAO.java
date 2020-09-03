package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.data.repository.CrudRepository;

public interface AnimalRequestDAO extends CrudRepository<AnimalRequest, Long> {
}
