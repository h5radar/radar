package com.h5radar.radar.domain.product;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.domain.AbstractServiceTests;

class ProductServiceRepositoryTests extends AbstractServiceTests {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProductService productService;

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithNullFilter() {
    List<Product> productList = List.of(
        new Product(null, "My title", "My description"),
        new Product(null, "My new title", "My new description"));
    for (Product product : productList) {
      productRepository.save(product);
    }

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<ProductDto> productDtoPage = productService.findAll(null, pageable);
    Assertions.assertEquals(10, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(2, productDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankTitleFilter() {
    List<Product> productList = List.of(
        new Product(null, "My title", "My description"),
        new Product(null, "My new title", "My new description"));
    for (Product product : productList) {
      productRepository.save(product);
    }

    ProductFilter productFilter = new ProductFilter();
    productFilter.setTitle("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<ProductDto> productDtoPage = productService.findAll(productFilter, pageable);
    Assertions.assertEquals(10, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(2, productDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithTitleFilter() {
    List<Product> productList = List.of(
        new Product(null,  "My title", "My description"),
        new Product(null, "My new title", "My new description"));
    for (Product product : productList) {
      productRepository.save(product);
    }

    ProductFilter productFilter = new ProductFilter();
    productFilter.setTitle(productList.iterator().next().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<ProductDto> productDtoPage = productService.findAll(productFilter, pageable);
    Assertions.assertEquals(10, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(1, productDtoPage.getNumberOfElements());
    Assertions.assertNotNull(productDtoPage.iterator().next().getId());
    Assertions.assertEquals(productDtoPage.iterator().next().getTitle(),
        productList.iterator().next().getTitle());
    Assertions.assertEquals(productDtoPage.iterator().next().getDescription(),
        productList.iterator().next().getDescription());
  }
}
