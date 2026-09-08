package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util;

import dev.pjosalgado.pocs.collectors.openapi.model.Link;
import dev.pjosalgado.pocs.collectors.openapi.model.PaginationResponse;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleDataWrapper;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.PurchaseDetails;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleResponseMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.record.PageLinksContext;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.record.TitleUpdateContext;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.HashMap;

@UtilityClass
public class ControllerUtils {

    public static final int DEFAULT_PAGE_SIZE = 10;
    private static final String BASE_PATH = "/registration/v1/titles";

    public static TitleDataWrapper buildTitleResponse(Title title, TitleResponseMapper mapper) {
        var response = new TitleDataWrapper();
        response.setData(mapper.fromTitle(title));
        response.setLinks(buildSingleTitleLinks(title.getTitleId()));
        return response;
    }

    public static Pageable buildPageable(Integer page, Integer pageSize) {
        return PageRequest.of(
                page != null ? page : 0,
                pageSize != null ? pageSize : DEFAULT_PAGE_SIZE
        );
    }

    public static PageLinksContext buildPageLinks(int currentPage, int pageSize, int totalPages) {
        var links = new HashMap<String, Link>();
        links.put("self", buildLink(buildPagePath(currentPage, pageSize)));

        if (currentPage > 0) {
            links.put("prev", buildLink(buildPagePath(currentPage - 1, pageSize)));
        }

        if (currentPage < totalPages - 1) {
            links.put("next", buildLink(buildPagePath(currentPage + 1, pageSize)));
        }

        if (totalPages > 0) {
            links.put("first", buildLink(buildPagePath(0, pageSize)));
            links.put("last", buildLink(buildPagePath(totalPages - 1, pageSize)));
        }

        return new PageLinksContext(currentPage, pageSize, totalPages, links);
    }

    public static PaginationResponse buildPagination(Page<?> page) {
        var pagination = new PaginationResponse();
        pagination.setPage(page.getNumber());
        pagination.setPageSize(page.getSize());
        pagination.setTotalElements((int) page.getTotalElements());
        pagination.setTotalPages(page.getTotalPages());
        return pagination;
    }

    public static Title buildTitleFromUpdateRequest(TitleUpdateContext context) {
        var request = context.updates();
        PurchaseDetails purchaseDetails = null;
        if (request.getPurchaseDetails() != null) {
            var pd = request.getPurchaseDetails();
            purchaseDetails = PurchaseDetails.builder()
                    .store(pd.getStore())
                    .price(pd.getPrice() != null ? BigDecimal.valueOf(pd.getPrice()) : null)
                    .currency(pd.getCurrency())
                    .build();
        }
        return Title.builder()
                .titleId(context.titleId())
                .name(request.getName())
                .originalName(request.getOriginalName())
                .studio(request.getStudio())
                .mediaFormat(request.getMediaFormat())
                .titleCategory(request.getTitleCategory())
                .barcode(request.getBarcode())
                .purchaseDetails(purchaseDetails)
                .build();
    }

    public static Title buildTitleFromUpdateRequest(String titleId,
                                                    dev.pjosalgado.pocs.collectors.openapi.model.TitleUpdateRequest request) {
        return buildTitleFromUpdateRequest(new TitleUpdateContext(titleId, request));
    }

    private static HashMap<String, Link> buildSingleTitleLinks(String titleId) {
        var links = new HashMap<String, Link>();
        links.put("self", buildLink(BASE_PATH + "/" + titleId));
        links.put("collection", buildLink(BASE_PATH));
        return links;
    }

    private static String buildPagePath(int page, int pageSize) {
        return BASE_PATH + "?page=" + page + "&pageSize=" + pageSize;
    }

    private static Link buildLink(String href) {
        Link link = new Link();
        link.setHref(href);
        return link;
    }

}
