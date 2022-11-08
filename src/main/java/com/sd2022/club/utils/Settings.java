package com.sd2022.club.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:config.properties")
@PropertySource("classpath:memcached.properties")
public class Settings {
    @Value("${cache.name}")
    public static String CACHE_NAME;

    @Value("${cache.server.adress}")
    public static String CACHE_SERVER_ADDRESS;


}
