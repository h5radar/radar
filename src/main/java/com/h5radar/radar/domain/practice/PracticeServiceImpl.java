package com.h5radar.radar.domain.practice;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar_user.RadarUser;


@RequiredArgsConstructor
@Service
@Transactional
public class PracticeServiceImpl implements PracticeService {

  private final Validator validator;
  private final PracticeRepository practiceRepository;
  private final PracticeMapper practiceMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<PracticeDto> findAll() {
    return practiceRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(practiceMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PracticeDto> findAll(PracticeFilter practiceFilter, Pageable pageable) {
    return practiceRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (practiceFilter != null && practiceFilter.getTitle() != null
          && !practiceFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), practiceFilter.getTitle()));
      }
      if (practiceFilter != null && practiceFilter.getWebsite() != null
          && !practiceFilter.getWebsite().isBlank()) {
        predicateList.add(builder.like(root.get("website"), practiceFilter.getWebsite()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(practiceMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PracticeDto> findById(Long id) {
    return practiceRepository.findById(id).map(practiceMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PracticeDto> findByTitle(String title) {
    return practiceRepository.findByTitle(title).map(practiceMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PracticeDto> findByRadarUserIdAndTitle(Long radarUserId, String title) {
    return practiceRepository.findByRadarUserIdAndTitle(radarUserId, title).map(practiceMapper::toDto);
  }

  @Override
  @Transactional
  public PracticeDto save(PracticeDto practiceDto) {
    Practice practice = practiceMapper.toEntity(practiceDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<Practice>> constraintViolationSet = validator.validate(practice);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Practice> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return practiceMapper.toDto(practiceRepository.save(practice));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    practiceRepository.deleteById(id);
  }

  @Override
  @Transactional
  public long deleteByRadarUserId(Long radarUserId) {
    return practiceRepository.deleteByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public long countByRadarUserId(Long radarUserId) {
    return this.practiceRepository.countByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public void seed(Long radarUserId) throws Exception {
    // Read practice_blips
    URL url = ResourceUtils.getURL("classpath:database/datasets/practice_en.csv");
    String fileContent = new BufferedReader(new InputStreamReader(url.openStream())).lines()
        .collect(Collectors.joining("\n"));

    String[] record = null;
    final RadarUser radarUser = new RadarUser(radarUserId);
    CSVReader csvReader = new CSVReaderBuilder(new StringReader(fileContent))
        .withCSVParser(new CSVParserBuilder().withSeparator('|').build())
        .withSkipLines(1).build();
    while ((record = csvReader.readNext()) != null) {
      Practice practice = new Practice();
      practice.setRadarUser(radarUser);
      practice.setTitle(record[0]);
      practice.setDescription(record[1]);

      // Create only if not exists
      if (this.practiceRepository.findByRadarUserIdAndTitle(radarUserId, practice.getTitle()).isEmpty()) {
        this.practiceRepository.save(practice);
      }
    }
  }
}
