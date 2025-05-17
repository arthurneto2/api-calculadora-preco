package com.arthur.api.util;

import java.time.Instant;
public class DateTimeFormarter {

    /**
     * Formato iso8601: 'yyyy-MM-dd hh:mm:ss'
     */

    public static String toIso8601(Instant instant){
        return instant.toString().replace("T", " ").replace("Z", "").split("\\.")[0];
    }

}