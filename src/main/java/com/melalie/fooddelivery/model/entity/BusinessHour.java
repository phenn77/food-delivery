package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "business_hours")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessHour implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @Column(name = "restaurant_id")
    private String restaurantId;

    @Column(name = "day")
    private String day;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String toTime;
}
