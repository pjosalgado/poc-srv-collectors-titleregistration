package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest;

import dev.pjosalgado.pocs.collectors.openapi.api.RegistrationApiDelegate;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateWrapper;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleDataWrapper;
import dev.pjosalgado.pocs.collectors.openapi.model.TitlePageWrapper;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleUpdateWrapper;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.CreateTitleUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.DeleteTitleUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.FindAllTitlesUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.FindTitleByIdUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.UpdateTitleUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleRequestMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleResponseMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.record.TitleUpdateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.ControllerUtils.buildPageLinks;
import static dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.ControllerUtils.buildPageable;
import static dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.ControllerUtils.buildPagination;
import static dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.ControllerUtils.buildTitleFromUpdateRequest;
import static dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.ControllerUtils.buildTitleResponse;

@Service
@RequiredArgsConstructor
public class TitleRegistrationController implements RegistrationApiDelegate {

    private final CreateTitleUseCase createTitleUseCase;
    private final FindTitleByIdUseCase findTitleByIdUseCase;
    private final FindAllTitlesUseCase findAllTitlesUseCase;
    private final UpdateTitleUseCase updateTitleUseCase;
    private final DeleteTitleUseCase deleteTitleUseCase;
    private final TitleRequestMapper titleRequestMapper;
    private final TitleResponseMapper titleResponseMapper;

    @Override
    public ResponseEntity<TitleDataWrapper> titleCreate(TitleCreateWrapper wrapper) {
        var title = titleRequestMapper.toTitle(wrapper.getData());
        var created = createTitleUseCase.execute(title);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildTitleResponse(created, titleResponseMapper));
    }

    @Override
    public ResponseEntity<TitlePageWrapper> titleFindAll(Integer page, Integer pageSize) {
        var pageable = buildPageable(page, pageSize);
        var result = findAllTitlesUseCase.execute(pageable);
        var response = new TitlePageWrapper();
        response.setData(result.getContent().stream()
                .map(titleResponseMapper::fromTitle)
                .collect(Collectors.toList()));
        response.setPagination(buildPagination(result));
        response.setLinks(buildPageLinks(result.getNumber(), result.getSize(), result.getTotalPages()).links());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TitleDataWrapper> titleFindById(String titleId) {
        var title = findTitleByIdUseCase.execute(titleId);
        return ResponseEntity.ok(buildTitleResponse(title, titleResponseMapper));
    }

    @Override
    public ResponseEntity<TitleDataWrapper> titleUpdate(String titleId, TitleUpdateWrapper wrapper) {
        var title = buildTitleFromUpdateRequest(new TitleUpdateContext(titleId, wrapper.getData()));
        var updated = updateTitleUseCase.execute(title);
        return ResponseEntity.ok(buildTitleResponse(updated, titleResponseMapper));
    }

    @Override
    public ResponseEntity<Void> titleDelete(String titleId) {
        deleteTitleUseCase.execute(titleId);
        return ResponseEntity.noContent().build();
    }

}
