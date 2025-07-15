package com.h5radar.radar.compliance;

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
public class ComplianceServiceImpl implements ComplianceService {

  private final Validator validator;
  private final ComplianceRepository complianceRepository;
  private final ComplianceMapper complianceMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<ComplianceDto> findAll() {
    return complianceRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
        .stream().map(complianceMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ComplianceDto> findAll(ComplianceFilter complianceFilter, Pageable pageable) {
    return complianceRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (complianceFilter != null && complianceFilter.getTitle() != null
          && !complianceFilter.getTitle().isBlank()) {
        predicateList.add(builder.like(root.get("title"), complianceFilter.getTitle()));
      }
      if (complianceFilter != null && complianceFilter.getWebsite() != null
          && !complianceFilter.getWebsite().isBlank()) {
        predicateList.add(builder.like(root.get("website"), complianceFilter.getWebsite()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(complianceMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ComplianceDto> findById(Long id) {
    return complianceRepository.findById(id).map(complianceMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ComplianceDto> findByTitle(String title) {
    return complianceRepository.findByTitle(title).map(complianceMapper::toDto);
  }

  @Override
  @Transactional
  public ComplianceDto save(ComplianceDto complianceDto) {
    Compliance compliance = complianceMapper.toEntity(complianceDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<Compliance>> constraintViolationSet = validator.validate(compliance);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<Compliance> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return complianceMapper.toDto(complianceRepository.save(compliance));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    complianceRepository.deleteById(id);
  }

  @Override
  @Transactional
  public long deleteByRadarUserId(Long radarUserId) {
    return complianceRepository.deleteByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public long countByRadarUserId(Long radarUserId) {
    return this.complianceRepository.countByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public void seed(Long radarUserId) throws Exception {
    // Read compliance_blips
    URL url = ResourceUtils.getURL("classpath:database/datasets/compliances_en.csv");
    String fileContent = new BufferedReader(new InputStreamReader(url.openStream())).lines()
        .collect(Collectors.joining("\n"));

    String[] record = null;
    final RadarUser radarUser = new RadarUser(radarUserId);
    CSVReader csvReader = new CSVReaderBuilder(new StringReader(fileContent))
        .withCSVParser(new CSVParserBuilder().withSeparator('|').build())
        .withSkipLines(1).build();
    while ((record = csvReader.readNext()) != null) {
      Compliance compliance = new Compliance();
      compliance.setRadarUser(radarUser);
      compliance.setTitle(record[0]);
      compliance.setDescription(record[1]);

      // Create only if not exists
      if (this.complianceRepository.findByRadarUserIdAndTitle(radarUserId, compliance.getTitle()).isEmpty()) {
        this.complianceRepository.save(compliance);
      }
    }
  }
}
