package com.h5radar.radar.domain.product;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Product API")
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("")
  public ResponseEntity<Collection<ProductDto>> index(
      @Valid ProductFilter productFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<ProductDto> productDtoPage =
        productService.findAll(productFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(productDtoPage.getContent());
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ProductDto> show(@PathVariable("id") Long id) {
    Optional<ProductDto> productRecord = productService.findById(id);
    if (productRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(productRecord.get());
  }

  @PostMapping
  public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
    productService.save(productDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ProductDto> update(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
    Optional<ProductDto> productRecord = productService.findById(id);
    if (productRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    productDto.setId(id);
    productService.save(productDto);
    return ResponseEntity.status(HttpStatus.OK).body(productDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    Optional<ProductDto> productRecord = productService.findById(id);
    if (productRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    productService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
