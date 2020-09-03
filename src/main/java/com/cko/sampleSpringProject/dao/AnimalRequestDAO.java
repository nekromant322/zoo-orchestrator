package com.cko.sampleSpringProject.dao;

import com.cko.sampleSpringProject.model.AnimalRequest;
import org.springframework.data.repository.CrudRepository;

public interface AnimalRequestDAO extends CrudRepository<AnimalRequest, Long> {
}
