package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.melalie.fooddelivery.util.constant.CommonUtils.formatDate;
import static com.melalie.fooddelivery.util.constant.Constants.HOUR;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class ResetServiceTest {

    @InjectMocks
    private ResetService resetService;

    @Mock
    private BusinessHourRepository businessHourRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserPurchaseRepository userPurchaseRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void test() throws ParseException {
        String fromTime = "5 AM";
        String toTime = "12:45 AM";

        LocalTime fromDate;
        LocalTime toDate;

        DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("[hh:mm a]" + "[h:mm a]" + "[h a]" + "[hh a]");
        try {
            fromDate = LocalTime.parse(fromTime, defaultFormat);
            toDate = LocalTime.parse(toTime, defaultFormat);
        } catch (Exception e) {
            fromDate = LocalTime.now();
            toDate = LocalTime.now();
        }
        Duration dur = Duration.between(fromDate, toDate);

        long hours = dur.toHours();
        if (hours < 0) {
            hours += 24;
        }
        System.out.println(hours);
    }
}
