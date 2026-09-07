package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleEnrichmentMessage {

    private String titleId;
    private String name;
    private String originalName;
    private String studio;

}
