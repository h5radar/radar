package com.h5radar.radar.domain;

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
public class DomainServiceImpl implements DomainService {
  private final Validator validator;
  private final MessageSource messageSource;
  private final DomainRepository domainRepository;
  private final TechnologyRepository technologyRepository;
  private final DomainMapper domainMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<DomainDto> findAll() {
    return domainRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(domainMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<DomainDto> findAll(DomainFilter domainFilter, Pageable pageable) {
    return domainRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (domainFilter != null && domainFilter.getTitle() != null
          && !domainFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), domainFilter.getTitle()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(domainMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<DomainDto> findById(Long id) {
    return domainRepository.findById(id).map(domainMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<DomainDto> findByTitle(String title) {
    return domainRepository.findByTitle(title).map(domainMapper::toDto);
  }


  @Override
  @Transactional
  public DomainDto save(DomainDto domainDto) {
    /* TODO: uncomment
    List<ModelError> modelErrorList = new LinkedList<>();
    Optional<Technology> radarOptional = technologyRepository.findById(domainDto.getRadarId());
    if (radarOptional.isPresent()) {
      Optional<Domain> domainOptional = Optional.empty();
      if (domainDto.getId() != null) {
        domainOptional = domainRepository.findById(domainDto.getId());
      }
      modelErrorList.addAll(
          new DomainActiveSaveApprover(messageSource, radarOptional.get(), domainOptional).approve());
    }
    */

    Domain domain = domainMapper.toEntity(domainDto);
    /* TODO: uncomment
    // Throw exception if violations are exists
    Set<ConstraintViolation<Domain>> constraintViolationSet = validator.validate(domain);
    if (!modelErrorList.isEmpty() || !constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Domain> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
     */
    return domainMapper.toDto(domainRepository.save(domain));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Optional<Domain> domainOptional = domainRepository.findById(id);
    if (domainOptional.isPresent()) {
      List<ModelError> modelErrorList = new LinkedList<>();
      modelErrorList.addAll(new DomainActiveDeleteApprover(messageSource, domainOptional.get()).approve());
      if (!modelErrorList.isEmpty()) {
        String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
        throw new ValidationException(errorMessage, modelErrorList);
      }
      domainRepository.deleteById(id);
    }
  }
}
