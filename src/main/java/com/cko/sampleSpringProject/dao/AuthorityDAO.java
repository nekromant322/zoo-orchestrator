package com.cko.sampleSpringProject.dao;

import com.cko.sampleSpringProject.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityDAO extends CrudRepository<Authority, Long> {
}
