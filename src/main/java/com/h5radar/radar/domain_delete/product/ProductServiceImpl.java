package com.h5radar.radar.domain.product;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  private final Validator validator;
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<ProductDto> findAll() {
    return productRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(productMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(ProductFilter productFilter, Pageable pageable) {
    return productRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (productFilter != null && productFilter.getTitle() != null
          && !productFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), productFilter.getTitle()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(productMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductDto> findById(Long id) {
    return productRepository.findById(id).map(productMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductDto> findByTitle(String title) {
    return productRepository.findByTitle(title).map(productMapper::toDto);
  }

  @Override
  @Transactional
  public ProductDto save(ProductDto productDto) {
    Product product = productMapper.toEntity(productDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<Product>> constraintViolationSet = validator.validate(product);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Product> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return productMapper.toDto(productRepository.save(product));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    productRepository.deleteById(id);
  }
}
