package com.h5radar.radar.technology;

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

import com.h5radar.radar.ModelError;
import com.h5radar.radar.ValidationException;
import com.h5radar.radar.radar_user.RadarUser;


@RequiredArgsConstructor
@Service
@Transactional
public class TechnologyServiceImpl implements TechnologyService {

  private final Validator validator;
  private final TechnologyRepository technologyRepository;
  private final TechnologyMapper technologyMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<TechnologyDto> findAll() {
    return technologyRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(technologyMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TechnologyDto> findAll(TechnologyFilter technologyFilter, Pageable pageable) {
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
  public Optional<TechnologyDto> findById(Long id) {
    return technologyRepository.findById(id).map(technologyMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TechnologyDto> findByTitle(String title) {
    return technologyRepository.findByTitle(title).map(technologyMapper::toDto);
  }

  @Override
  @Transactional
  public TechnologyDto save(TechnologyDto technologyDto) {
    Technology technology = technologyMapper.toEntity(technologyDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<Technology>> constraintViolationSet = validator.validate(technology);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Technology> constraintViolation : constraintViolationSet) {
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

  @Override
  @Transactional
  public long deleteByRadarUserId(Long radarUserId) {
    return technologyRepository.deleteByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public long countByRadarUserId(Long radarUserId) {
    return this.technologyRepository.countByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public void seed(Long radarUserId) throws Exception {
    // Read technology_blips
    URL url = ResourceUtils.getURL("classpath:database/datasets/technologies_en.csv");
    String fileContent = new BufferedReader(new InputStreamReader(url.openStream())).lines()
        .collect(Collectors.joining("\n"));

    String[] record = null;
    final RadarUser radarUser = new RadarUser(radarUserId);
    CSVReader csvReader = new CSVReaderBuilder(new StringReader(fileContent))
        .withCSVParser(new CSVParserBuilder().withSeparator('|').build())
        .withSkipLines(1).build();
    while ((record = csvReader.readNext()) != null) {
      Technology technology = new Technology();
      technology.setRadarUser(radarUser);
      technology.setTitle(record[0]);
      technology.setWebsite(record[1]);
      technology.setDescription(record[2]);

      // Create only if not exists
      if (this.technologyRepository.findByRadarUserIdAndTitle(radarUserId, technology.getTitle()).isEmpty()) {
        this.technologyRepository.save(technology);
      }
    }
  }
}
