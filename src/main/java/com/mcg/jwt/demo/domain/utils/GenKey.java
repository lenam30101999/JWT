package com.mcg.jwt.demo.domain.utils;

import lombok.Data;

@Data
public class GenKey {
    public static final String KEY_REFRESH = "refresh_";
    public static final String KEY_ACCESS = "access_";

    public static String genRefreshKey(String token){
        return KEY_REFRESH + token;
    }

    public static String genAccessKey(String token){
        return KEY_ACCESS + token;
    }
}
