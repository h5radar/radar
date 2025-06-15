package com.h5radar.radar.domain.license;

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
public class LicenseServiceImpl implements LicenseService {

  private final Validator validator;
  private final LicenseRepository licenseRepository;
  private final LicenseMapper licenseMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<LicenseDto> findAll() {
    return licenseRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(licenseMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<LicenseDto> findAll(LicenseFilter licenseFilter, Pageable pageable) {
    return licenseRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (licenseFilter != null && licenseFilter.getTitle() != null
          && !licenseFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), licenseFilter.getTitle()));
      }
      if (licenseFilter != null && licenseFilter.getWebsite() != null
          && !licenseFilter.getWebsite().isBlank()) {
        predicateList.add(builder.like(root.get("website"), licenseFilter.getWebsite()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(licenseMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<LicenseDto> findById(Long id) {
    return licenseRepository.findById(id).map(licenseMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<LicenseDto> findByTitle(String title) {
    return licenseRepository.findByTitle(title).map(licenseMapper::toDto);
  }

  @Override
  @Transactional
  public LicenseDto save(LicenseDto licenseDto) {
    License license = licenseMapper.toEntity(licenseDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<License>> constraintViolationSet = validator.validate(license);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<License> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return licenseMapper.toDto(licenseRepository.save(license));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    licenseRepository.deleteById(id);
  }
}
