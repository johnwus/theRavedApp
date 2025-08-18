package com.raved.analytics.mapper;

import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.model.UserMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMetricsMapper {
    
    UserMetricsResponse toUserMetricsResponse(UserMetrics userMetrics);
    
    UserMetrics toUserMetrics(UserMetricsResponse response);
} 