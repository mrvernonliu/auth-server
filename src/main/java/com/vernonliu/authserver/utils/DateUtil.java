package com.vernonliu.authserver.utils;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class DateUtil {

    private static final long HOUR_MULTIPLIER = 60000;

    public static Date getDatePlusHours(int hours) {
        Calendar now = Calendar.getInstance();
        return new Date(now.getTimeInMillis() + hours*HOUR_MULTIPLIER);
    }
}
