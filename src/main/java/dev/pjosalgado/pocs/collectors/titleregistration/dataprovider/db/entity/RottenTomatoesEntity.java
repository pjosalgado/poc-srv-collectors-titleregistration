package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class RottenTomatoesEntity {

    private Integer tomatometer;
    private String criticRating;

}
