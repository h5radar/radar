package com.t9radar.radar.domain.product;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  Collection<ProductDto> findAll();

  Page<ProductDto> findAll(ProductFilter productFilter, Pageable pageable);

  Optional<ProductDto> findById(Long id);

  Optional<ProductDto> findByTitle(String title);

  ProductDto save(ProductDto productDto);

  void deleteById(Long id);
}
