package com.h5radar.radar.domain.maturity;

import jakarta.validation.Valid;
import java.util.Collection;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.spmaturityframework.data.domain.Page;
import org.spmaturityframework.data.domain.PageRequest;
import org.spmaturityframework.data.domain.Sort;
import org.spmaturityframework.web.bind.annotation.GetMapping;
import org.spmaturityframework.web.bind.annotation.RequestMapping;
import org.spmaturityframework.web.bind.annotation.RequestParam;
import org.spmaturityframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Maturity API")
@RequestMapping("/api/v1/maturities")
@RequiredArgsConstructor
public class MaturityController {

  private final MaturityService maturityService;

  @GetMapping("")
  public Collection<MaturityDto> index(@Valid MaturityFilter maturityFilter,
                             @RequestParam(defaultValue = "${application.paging.page}") int page,
                             @RequestParam(defaultValue = "${application.paging.size}") int size,
                             @RequestParam(defaultValue = "title,asc") Stmaturity[] sort) {
    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<MaturityDto> maturityDtoPage = maturityService.findAll(maturityFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return maturityDtoPage.getContent();
  }
}
