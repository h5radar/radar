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
  private final RadarUserMapper radarUserMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<RadarUserDto> findAll() {
    return technologyRepository.findAll(Sort.by(Sort.Direction.ASC, "sub"))
        .stream().map(radarUserMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RadarUserDto> findAll(RadarUserFilter technologyFilter, Pageable pageable) {
    return technologyRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (technologyFilter != null && technologyFilter.getSub() != null
          && !technologyFilter.getSub().isBlank()) {
        predicateList.add(builder.like(root.get("sub"), technologyFilter.getSub()));
      }
      if (technologyFilter != null && technologyFilter.getUsername() != null
          && !technologyFilter.getUsername().isBlank()) {
        predicateList.add(builder.like(root.get("username"), technologyFilter.getUsername()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findById(Long id) {
    return technologyRepository.findById(id).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findBySub(String sub) {
    return technologyRepository.findBySub(sub).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional
  public RadarUserDto save(RadarUserDto technologyDto) {
    RadarUser technology = radarUserMapper.toEntity(technologyDto);
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
    return radarUserMapper.toDto(technologyRepository.save(technology));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    technologyRepository.deleteById(id);
  }
}
