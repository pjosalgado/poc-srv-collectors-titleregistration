package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class TitleEntity {

    @Id
    private String titleId;

    private String name;
    private String originalName;
    private String studio;
    private TitleType type;
    private String barcode;
    private PurchaseDetailsEntity purchaseDetails;

    @CreatedDate
    private LocalDateTime createdDateTime;

}
