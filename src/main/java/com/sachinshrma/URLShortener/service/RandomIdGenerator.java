package com.sachinshrma.URLShortener.service;

import com.sachinshrma.URLShortener.dao.SharedCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;

@Slf4j
@Service
public class RandomIdGenerator {
    private long MINIMUM_VALUE;
    private long MAXIMUM_VALUE;
    private long TOTAL_AVAILABLE_VALUES;
    private final long MAXIMUM_ALLOWED_VALUES = 100000L;
    private final long MINIMUM_THRESHOLD = 10000L;
    private final Random random;
    private final HashSet<Long> cache;
    @Autowired
    MongoOperations mongoOperations;

    public RandomIdGenerator() {
        this.random = new Random();
        this.cache = new HashSet<>();
    }

    public void updateRange() {
        Update update = new Update();
        update.inc("counter");
        SharedCounter counter = mongoOperations.findAndModify(
                Query.query(new Criteria("_id").is("1")),
                update,
                FindAndModifyOptions.options().upsert(true).returnNew(true),
                SharedCounter.class);
        log.info("Counter: {}", counter.getCounter());
        MINIMUM_VALUE = (long) Math.pow(62, 6) + (counter.getCounter() * MAXIMUM_ALLOWED_VALUES);
        MAXIMUM_VALUE = MINIMUM_VALUE + MAXIMUM_ALLOWED_VALUES - 1;
        TOTAL_AVAILABLE_VALUES = MAXIMUM_ALLOWED_VALUES;
    }

    public long getRandomId() {
        if (TOTAL_AVAILABLE_VALUES < MINIMUM_THRESHOLD) {
            updateRange();
        }
        long id =  random.nextLong(MAXIMUM_VALUE - MINIMUM_VALUE + 1) + MINIMUM_VALUE;
        if (cache.contains(id)) {
            return getRandomId();
        }
        cache.add(id);
        --TOTAL_AVAILABLE_VALUES;
        log.info("MINIMUM: {}, MAXIMUM: {}, TOTAL_AVAILABLE_VALUES{}", MINIMUM_VALUE, MAXIMUM_VALUE, TOTAL_AVAILABLE_VALUES);
        return id;
    }
}
