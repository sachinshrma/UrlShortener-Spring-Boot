package com.sachinshrma.URLShortener.repository;

import com.sachinshrma.URLShortener.dao.SharedCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SharedCounterRepository extends MongoRepository<SharedCounter, String> {

}
