package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest;

import dev.pjosalgado.pocs.collectors.openapi.api.RegistrationApiDelegate;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreatedResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.CreateTitleUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleCreateRequestMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleCreatedResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TitleRegistrationController implements RegistrationApiDelegate {

    private final CreateTitleUseCase createTitleUseCase;
    private final TitleCreateRequestMapper titleCreateRequestMapper;
    private final TitleCreatedResponseMapper titleCreatedResponseMapper;

    @Override
    public ResponseEntity<TitleCreatedResponse> titleCreate(TitleCreateRequest request) {
        var titleToCreate = titleCreateRequestMapper.toTitle(request);
        var createdTitle = createTitleUseCase.execute(titleToCreate);
        var response = titleCreatedResponseMapper.fromTitle(createdTitle);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

}
