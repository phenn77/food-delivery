package com.melalie.fooddelivery.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.melalie.fooddelivery.model.dto.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenedRestaurantResponse implements Serializable {

    private String type;
    private Integer openHours;
    private List<Schedule> restaurants;
}
