package com.raved.analytics.mapper;

import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.model.ContentMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ContentMetricsMapper {
    
    ContentMetricsResponse toContentMetricsResponse(ContentMetrics contentMetrics);
    
    ContentMetrics toContentMetrics(ContentMetricsResponse response);
} 