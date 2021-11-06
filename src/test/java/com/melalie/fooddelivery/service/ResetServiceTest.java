package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.repository.MenuRepository;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import com.melalie.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class ResetServiceTest {

    @InjectMocks
    private ResetService resetService;

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
    public void test() {
//        String businessHours = "Sunday: 10:45 AM - 5 PM | Monday, Friday: 2:30 PM - 8 PM | Tuesday: 11 AM - 2 PM | Wednesday: 1:15 PM - 3:15 AM | Thursday: 10 AM - 3:15 AM | Saturday: 5 AM - 11:30 AM";
//
//        String[] result = businessHours.split("\\|");
//
//        System.out.println(Arrays.toString(result));


        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
            date = sdf.parse("2020-02-10 04:09:00 UTC");
        } catch (Exception e) {
            System.out.println("Error");
        }

        Date result = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
            result = sdf.parse("2020-02-10 04:09:00 UTC");
        } catch (Exception e) {
        }


        System.out.println(new Timestamp(result.getTime()));
    }
}
