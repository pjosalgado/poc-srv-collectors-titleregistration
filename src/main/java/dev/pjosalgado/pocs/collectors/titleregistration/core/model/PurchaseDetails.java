package dev.pjosalgado.pocs.collectors.titleregistration.core.model;

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PurchaseDetails {

    private String store;
    private BigDecimal price;
    private MonetaryType currency;

}
