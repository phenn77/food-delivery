package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessHour implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "restaurant_id")
    private String restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "day")
    private String day;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String toTime;

    @Column(name = "open_time")
    private String openTime;
}
