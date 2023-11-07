package com.sachinshrma.URLShortener.dao;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("sharedcounter")
public class SharedCounter {
    private String _id;
    private long counter;
}
