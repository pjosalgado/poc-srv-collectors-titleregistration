package dev.pjosalgado.pocs.collectors.titleregistration.core.model;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Title {

    private String titleId;
    private String name;
    private String originalName;
    private String studio;
    private TitleType type;
    private String barcode;
    private PurchaseDetails purchaseDetails;
    private LocalDateTime createdDateTime;

    public void generateId() {
        this.titleId = UUID.randomUUID().toString();
    }

    public void applyUpdates(Title updates) {
        if (updates.name != null) this.name = updates.name;
        if (updates.originalName != null) this.originalName = updates.originalName;
        if (updates.studio != null) this.studio = updates.studio;
        if (updates.type != null) this.type = updates.type;
        if (updates.barcode != null) this.barcode = updates.barcode;
        if (updates.purchaseDetails != null) this.purchaseDetails = updates.purchaseDetails;
    }

}
