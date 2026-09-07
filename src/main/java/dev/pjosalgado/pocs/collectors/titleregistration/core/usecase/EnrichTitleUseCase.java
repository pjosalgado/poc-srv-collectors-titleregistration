package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.record.TitleEnrichmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EnrichTitleUseCase {

    public void execute(TitleEnrichmentRequest request) {
        log.info("Enriching title: titleId={}", request.titleId());
    }

}
