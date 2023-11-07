package com.sachinshrma.URLShortener.repository;

import com.sachinshrma.URLShortener.dao.URLRecord;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface URLRecordRepository extends MongoRepository<URLRecord, String> {

    @Query("{_id:'?0'}")
    URLRecord findURLRecordById(String id);

    @Cacheable(value = "longUrlCache", key = "#shortUrl")
    @Query("{shortUrl:'?0'}")
    URLRecord findURLRecordByShortUrl(String shortUrl);
}
