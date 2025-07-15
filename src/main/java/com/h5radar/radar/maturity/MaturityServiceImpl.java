package com.h5radar.radar.maturity;

import jakarta.persistence.criteria.Predicate;
// import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
// import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;
// import com.h5radar.radar.technology.Technology;
import com.h5radar.radar.technology.TechnologyRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MaturityServiceImpl implements MaturityService {
  private final Validator validator;
  private final MessageSource messageSource;
  private final MaturityRepository maturityRepository;
  private final TechnologyRepository technologyRepository;
  private final MaturityMapper maturityMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<MaturityDto> findAll() {
    return maturityRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(maturityMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MaturityDto> findAll(MaturityFilter maturityFilter, Pageable pageable) {
    return maturityRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (maturityFilter != null && maturityFilter.getTitle() != null
          && !maturityFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), maturityFilter.getTitle()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(maturityMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<MaturityDto> findById(Long id) {
    return maturityRepository.findById(id).map(maturityMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<MaturityDto> findByTitle(String title) {
    return maturityRepository.findByTitle(title).map(maturityMapper::toDto);
  }

  @Override
  @Transactional
  public MaturityDto save(MaturityDto maturityDto) {
    /* TODO: uncomment
    List<ModelError> modelErrorList = new LinkedList<>();
    Optional<Radar> radarOptional = technologyRepository.findById(maturityDto.getRadarId());
    if (radarOptional.isPresent()) {
      Optional<Maturity> maturityOptional = Optional.empty();
      if (maturityDto.getId() != null) {
        maturityOptional = maturityRepository.findById(maturityDto.getId());
      }
      modelErrorList.addAll(
          new RadarActiveSaveApprover(messageSource, radarOptional.get(), maturityOptional).approve());
    }
     */

    Maturity maturity = maturityMapper.toEntity(maturityDto);
    /* TODO: uncomment
    // Throw exception if violations are exists
    Set<ConstraintViolation<Maturity>> constraintViolationSet = validator.validate(maturity);
    if (!modelErrorList.isEmpty() || !constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Maturity> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
     */
    return maturityMapper.toDto(maturityRepository.save(maturity));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Optional<Maturity> maturityOptional = maturityRepository.findById(id);
    if (maturityOptional.isPresent()) {
      List<ModelError> modelErrorList = new LinkedList<>();
      modelErrorList.addAll(new MaturityActiveDeleteApprover(messageSource, maturityOptional.get()).approve());
      if (!modelErrorList.isEmpty()) {
        String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
        throw new ValidationException(errorMessage, modelErrorList);
      }
      maturityRepository.deleteById(id);
    }
  }
}
