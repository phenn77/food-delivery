package com.melalie.fooddelivery.model.response;

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
public class OpenedRestaurantResponse implements Serializable {

    private List<Schedule> restaurants;
}
