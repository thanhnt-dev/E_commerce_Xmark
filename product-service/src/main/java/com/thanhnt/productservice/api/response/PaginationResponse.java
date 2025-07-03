package com.thanhnt.productservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PaginationResponse<T> {
  private T metadata;
  private Long maxPage;
  private Integer nextPage;
  private Integer currentPage;
  private Integer previousPage;
  private Long total;
  private boolean status;

  public static <T> PaginationResponse<T> build(
      T data, Page<?> pageList, int page, boolean status) {
    long total = pageList.getTotalElements();
    boolean hasPrev = pageList.hasPrevious();
    boolean hasNext = pageList.hasNext();

    Integer next = null;
    Integer prev = null;

    long maxPage = pageList.getTotalPages();

    if (hasNext) next = pageList.nextPageable().getPageNumber() + 1;
    if (hasPrev) prev = pageList.previousPageable().getPageNumber() + 1;

    return (PaginationResponse<T>)
        PaginationResponse.builder()
            .metadata(data)
            .maxPage(maxPage)
            .nextPage(next)
            .currentPage(page)
            .previousPage(prev)
            .total(total)
            .status(status)
            .build();
  }

  public static <T> PaginationResponse<T> buildFromElastic(
      T data, int currentPage, int pageSize, long total, boolean status) {

    long maxPage = (long) Math.ceil((double) total / pageSize);
    Integer next = (currentPage < maxPage) ? currentPage + 1 : null;
    Integer prev = (currentPage > 1) ? currentPage - 1 : null;

    return PaginationResponse.<T>builder()
        .metadata(data)
        .maxPage(maxPage)
        .nextPage(next)
        .currentPage(currentPage)
        .previousPage(prev)
        .total(total)
        .status(status)
        .build();
  }
}
