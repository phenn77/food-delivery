package com.melalie.fooddelivery.model.response;

import com.melalie.fooddelivery.model.dto.SearchResult;
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
public class SearchResponse implements Serializable {

    private List<SearchResult> result;
}
