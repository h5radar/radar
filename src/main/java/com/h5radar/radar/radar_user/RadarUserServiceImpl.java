package com.h5radar.radar.radar_user;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;

@RequiredArgsConstructor
@Service
@Transactional
public class RadarUserServiceImpl implements RadarUserService {

  private final Validator validator;
  private final MessageSource messageSource;
  private final RadarUserRepository radarUserRepository;
  private final RadarUserMapper radarUserMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<RadarUserDto> findAll() {
    return radarUserRepository.findAll(Sort.by(Sort.Direction.ASC, "sub"))
        .stream().map(radarUserMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RadarUserDto> findAll(RadarUserFilter radarUserFilter, Pageable pageable) {
    return radarUserRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (radarUserFilter != null && radarUserFilter.getSub() != null
          && !radarUserFilter.getSub().isBlank()) {
        predicateList.add(builder.like(root.get("sub"), radarUserFilter.getSub()));
      }
      if (radarUserFilter != null && radarUserFilter.getUsername() != null
          && !radarUserFilter.getUsername().isBlank()) {
        predicateList.add(builder.like(root.get("username"), radarUserFilter.getUsername()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findById(Long id) {
    return radarUserRepository.findById(id).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RadarUserDto> findBySub(String sub) {
    return radarUserRepository.findBySub(sub).map(radarUserMapper::toDto);
  }

  @Override
  @Transactional
  public RadarUserDto save(RadarUserDto radarUserDto) {
    RadarUser radarUser = radarUserMapper.toEntity(radarUserDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<RadarUser>> constraintViolationSet = validator.validate(radarUser);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<RadarUser> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return radarUserMapper.toDto(radarUserRepository.save(radarUser));
  }

  @Transactional
  public void updateSeed(Long id) {
    RadarUser radarUser = radarUserRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException(
            messageSource.getMessage("radar_user.error.not_found",
                new Object[]{id}, LocaleContextHolder.getLocale())));
    if (!radarUser.isSeeded()) {
      // Update seed info
      radarUser.setSeeded(true);
      if (radarUser.getSeededDate() == null) {
        radarUser.setSeededDate(Instant.now());
      }
      radarUserRepository.save(radarUser);
    }
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    radarUserRepository.deleteById(id);
  }
}
