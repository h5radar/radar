package com.h5radar.radar.domain.product;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.domain.AbstractServiceTests;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;


class ProductServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository radarUserRepository;

  @MockitoBean
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private ProductService productService;

  @Test
  void shouldFindAllProducts() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Product product = new Product();
    product.setId(10L);
    product.setRadarUser(radarUser);
    product.setTitle("My product");
    product.setDescription("My description");

    List<Product> productList = List.of(product);
    Mockito.when(productRepository.findAll(any(Sort.class))).thenReturn(productList);

    Collection<ProductDto> productDtoCollection = productService.findAll();
    Assertions.assertEquals(1, productDtoCollection.size());
    Assertions.assertEquals(productDtoCollection.iterator().next().getId(), product.getId());
    Assertions.assertEquals(productDtoCollection.iterator().next().getTitle(), product.getTitle());
    Assertions.assertEquals(productDtoCollection.iterator().next().getDescription(), product.getDescription());
  }

  @Test
  void shouldFindAllProductsWithEmptyFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Product product = new Product();
    product.setId(10L);
    product.setRadarUser(radarUser);
    product.setTitle("My product");
    product.setDescription("My description");

    List<Product> productList = List.of(product);
    Page<Product> page = new PageImpl<>(productList);
    Mockito.when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), any(Pageable.class)))
        .thenReturn(page);

    ProductFilter productFilter = new ProductFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<ProductDto> productDtoPage = productService.findAll(productFilter, pageable);
    Assertions.assertEquals(1, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(productDtoPage.iterator().next().getId(), product.getId());
    Assertions.assertEquals(productDtoPage.iterator().next().getTitle(), product.getTitle());
    Assertions.assertEquals(productDtoPage.iterator().next().getDescription(), product.getDescription());

    // Mockito.verify(productRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  /* TODO:
  @Test
  @Transactional
  void shouldFindAllProductsWithNullFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<Product> productList = List.of(
        new Product(null, radarUser, "My title", "My description"),
        new Product(null, radarUser, "My new title", "My new description"));
    productRepository.saveAll(productList);

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<ProductDto> productDtoPage = productService.findAll(null, pageable);
    Assertions.assertEquals(10, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(2, productDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllProductsWithBlankTitleFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<Product> productList = List.of(
        new Product(null, radarUser, "My title", "My description"),
        new Product(null, radarUser, "My new title", "My new description"));
    productRepository.saveAll(productList);

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
  void shouldFindAllProductsWithTitleFilter() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    List<Product> productList = List.of(
        new Product(null, radarUser, "My title", "My description"),
        new Product(null, radarUser, "My new title", "My new description"));
    productRepository.saveAll(productList);

    ProductFilter productFilter = new ProductFilter();
    productFilter.setTitle(productList.getFirst().getTitle());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
    Page<ProductDto> productDtoPage = productService.findAll(productFilter, pageable);
    Assertions.assertEquals(10, productDtoPage.getSize());
    Assertions.assertEquals(0, productDtoPage.getNumber());
    Assertions.assertEquals(1, productDtoPage.getTotalPages());
    Assertions.assertEquals(1, productDtoPage.getNumberOfElements());
    Assertions.assertNotNull(productDtoPage.iterator().next().getId());
    Assertions.assertEquals(productDtoPage.iterator().next().getTitle(),
        productList.getFirst().getTitle());
    Assertions.assertEquals(productDtoPage.iterator().next().getDescription(),
        productList.getFirst().getDescription());
  }
   */

  @Test
  void shouldFindByIdProduct() {
    final Product product = new Product();
    product.setId(10L);
    product.setTitle("My product");
    product.setDescription("My description");

    Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    Optional<ProductDto> productDtoOptional = productService.findById(product.getId());
    Assertions.assertTrue(productDtoOptional.isPresent());
    Assertions.assertEquals(product.getId(), productDtoOptional.get().getId());
    Assertions.assertEquals(product.getTitle(), productDtoOptional.get().getTitle());
    Assertions.assertEquals(product.getDescription(), productDtoOptional.get().getDescription());

    Mockito.verify(productRepository).findById(product.getId());
  }

  @Test
  void shouldFindByTitleProduct() {
    final Product product = new Product();
    product.setId(10L);
    product.setTitle("My product");
    product.setDescription("My description");

    Mockito.when(productRepository.findByTitle(product.getTitle())).thenReturn(Optional.of(product));

    Optional<ProductDto> productDtoOptional = productService.findByTitle(product.getTitle());
    Assertions.assertTrue(productDtoOptional.isPresent());
    Assertions.assertEquals(product.getId(), productDtoOptional.get().getId());
    Assertions.assertEquals(product.getTitle(), productDtoOptional.get().getTitle());
    Assertions.assertEquals(product.getDescription(), productDtoOptional.get().getDescription());

    Mockito.verify(productRepository).findByTitle(product.getTitle());
  }

  @Test
  void shouldSaveProduct() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Product product = new Product();
    product.setId(10L);
    product.setRadarUser(radarUser);
    product.setTitle("My product");
    product.setDescription("My description");

    Mockito.when(radarUserRepository.findById(any())).thenReturn(Optional.of(radarUser));
    Mockito.when(productRepository.save(any())).thenReturn(product);

    ProductDto productDto = productService.save(productMapper.toDto(product));
    Assertions.assertEquals(product.getId(), productDto.getId());
    Assertions.assertEquals(product.getTitle(), productDto.getTitle());
    Assertions.assertEquals(product.getDescription(), productDto.getDescription());

    Mockito.verify(radarUserRepository).findById(radarUser.getId());
    Mockito.verify(productRepository).save(any());
  }

  @Test
  void shouldFailToSaveProductDueToTitleWithWhiteSpace() {
    final Product product = new Product();
    product.setId(10L);
    product.setTitle(" My product ");
    product.setDescription("My description");

    ValidationException exception = catchThrowableOfType(() ->
        productService.save(productMapper.toDto(product)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteProduct() {
    final Product product = new Product();
    product.setId(10L);
    product.setTitle("My product");
    product.setDescription("My description");

    Mockito.doAnswer((i) -> null).when(productRepository).deleteById(product.getId());

    productService.deleteById(product.getId());
    Mockito.verify(productRepository).deleteById(product.getId());
  }
}
