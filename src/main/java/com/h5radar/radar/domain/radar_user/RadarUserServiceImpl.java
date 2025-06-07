package com.h5radar.radar.domain.radar_user;

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
public class RadarUserServiceImpl implements RadarUserService {

  private final Validator validator;
  private final RadarUserRepository technologyRepository;
  private final RadarUserMapper technologyMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<RadarUserDto> findAll() {
    return technologyRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(technologyMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RadarUserDto> findAll(RadarUserFilter technologyFilter, Pageable pageable) {
    return technologyRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (technologyFilter != null && technologyFilter.getTitle() != null
          && !technologyFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), technologyFilter.getTitle()));
      }
      if (technologyFilter != null && technologyFilter.getWebsite() != null
          && !technologyFilter.getWebsite().isBlank()) {
        predicateList.add(builder.like(root.get("website"), technologyFilter.getWebsite()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(technologyMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findById(Long id) {
    return technologyRepository.findById(id).map(technologyMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findByTitle(String title) {
    return technologyRepository.findByTitle(title).map(technologyMapper::toDto);
  }

  @Override
  @Transactional
  public RadarUserDto save(RadarUserDto technologyDto) {
    RadarUser technology = technologyMapper.toEntity(technologyDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<RadarUser>> constraintViolationSet = validator.validate(technology);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<RadarUser> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return technologyMapper.toDto(technologyRepository.save(technology));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    technologyRepository.deleteById(id);
  }
}
