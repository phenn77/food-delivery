package com.melalie.fooddelivery.util.constant;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class CommonUtils {

    public CommonUtils() {

    }

    public static String formatDate(String payload) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = sdf.parse(payload);

            return newFormat.format(date);
        } catch (Exception e) {
            log.error("Failed to convert date. Payload: {}", payload);

            throw new Exception("Error parsing date.");
        }
    }
}
