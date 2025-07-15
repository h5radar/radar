package com.h5radar.radar.domain;

import jakarta.validation.Valid;
import java.util.Collection;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Domain API")
@RequestMapping("/api/v1/domains")
@RequiredArgsConstructor
public class DomainController {

  private final DomainService domainService;

  @GetMapping("")
  public Collection<DomainDto> index(@Valid DomainFilter domainFilter,
                                      @RequestParam(defaultValue = "${application.paging.page}") int page,
                                      @RequestParam(defaultValue = "${application.paging.size}") int size,
                                      @RequestParam(defaultValue = "title,asc") String[] sort) {
    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<DomainDto> domainPage =
        domainService.findAll(domainFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return domainPage.getContent();
  }
}
