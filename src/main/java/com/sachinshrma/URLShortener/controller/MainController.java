package com.sachinshrma.URLShortener.controller;

import com.sachinshrma.URLShortener.dao.URLRecord;
import com.sachinshrma.URLShortener.model.ShortRequestModel;
import com.sachinshrma.URLShortener.repository.URLRecordRepository;
import com.sachinshrma.URLShortener.service.RandomIdGenerator;
import com.sachinshrma.URLShortener.util.EncoderDecoder;
import com.sachinshrma.URLShortener.util.URLUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
public class MainController {

    @Autowired
    private RandomIdGenerator randomIdGenerator;
    @Autowired
    private URLRecordRepository urlRecordRepository;

    @PostMapping("/short")
    public ResponseEntity<?> createShortUrl(@RequestBody ShortRequestModel shortRequestModel, HttpServletRequest request) {
        log.info("Long url in request: {}", shortRequestModel.getLongUrl());
        final String longUrl = shortRequestModel.getLongUrl();
        final long randomId = randomIdGenerator.getRandomId();
        if (randomId == -1) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Short url pool exhausted");
        }
        final String shortUrl = EncoderDecoder.toBase62(randomId);

        final String host = URLUtil.getRequestHostUrl(request);

        final URLRecord savedRecord = urlRecordRepository.save(new URLRecord(shortUrl, longUrl));
        savedRecord.setShortUrl(host + "/" + savedRecord.getShortUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedRecord);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToLongUrl(@PathVariable String shortUrl) {
        final URLRecord record = urlRecordRepository.findURLRecordByShortUrl(shortUrl);

        if (Objects.nonNull(record) && !StringUtils.isEmpty(record.getLongUrl())) {
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, record.getLongUrl()).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid URL");
    }
}
