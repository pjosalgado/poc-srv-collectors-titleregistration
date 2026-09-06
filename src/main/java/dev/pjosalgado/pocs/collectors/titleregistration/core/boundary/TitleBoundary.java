package dev.pjosalgado.pocs.collectors.titleregistration.core.boundary;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TitleBoundary {

    Title create(Title title);

    Optional<Title> findById(String titleId);

    Page<Title> findAll(Pageable pageable);

    Title update(Title title);

    void deleteById(String titleId);

}
