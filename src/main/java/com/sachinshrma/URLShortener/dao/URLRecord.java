package com.sachinshrma.URLShortener.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Document("urlmappings")
public class URLRecord implements Serializable {
    @JsonIgnore
    private String _id;
    private String shortUrl;
    private String longUrl;

    public URLRecord(String shortUrl, String longUrl) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }
}
