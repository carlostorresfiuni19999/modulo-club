package com.sd2022.club.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:memcached.properties")
public class Settings {

    public static final String CACHE_NAME = "platform-cache";

    @Value("${cache.name}")
    private String cacheName;


    public static final String CRON = "0 15 * * * *";

    public static final boolean ALLOW = true;
    public static final boolean REJECT = false;


    public String getCacheName() {
        return cacheName;
    }
}
