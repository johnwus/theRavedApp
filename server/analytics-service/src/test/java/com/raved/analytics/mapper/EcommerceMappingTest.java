package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.model.EventType;
import com.raved.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class EcommerceMappingTest {

    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void ecommerceData_isMapped_andComputedFieldsSet() {
        TrackEventRequest req = new TrackEventRequest();
        req.setSessionId("s-ecom");
        req.setEventType(EventType.PRODUCT_PURCHASE);
        var ecom = new TrackEventRequest.EcommerceDataRequest();
        ecom.setProductId("p1");
        ecom.setProductName("Shirt");
        ecom.setCategory("Apparel");
        ecom.setPrice(new BigDecimal("19.99"));
        ecom.setCurrency("USD");
        ecom.setQuantity(2);
        req.setEcommerceData(ecom);

        AnalyticsEvent entity = mapper.toEntity(req);
        assertThat(entity.getEcommerceData()).isNotNull();
        assertThat(entity.getEcommerceData().getProductId()).isEqualTo("p1");
        assertThat(entity.getEcommerceData().getPrice()).isEqualTo(19.99);
        assertThat(entity.getEventDate()).isNotBlank();
        assertThat(entity.getEventHour()).isBetween(0, 23);
        assertThat(entity.getEventWeekday()).isBetween(1, 7);
        assertThat(entity.getEventMonth()).isBetween(1, 12);
    }
}

