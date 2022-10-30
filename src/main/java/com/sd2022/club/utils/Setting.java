package com.sd2022.club.utils;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class Setting {
    @Value("${pagesize}")
    private static String size_str;

    @Getter
    private static final int pageSize = Integer.parseInt(size_str);
}
