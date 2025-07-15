package com.h5radar.radar.domain.license;

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

  @Override
  @Transactional
  public long deleteByRadarUserId(Long radarUserId) {
    return licenseRepository.deleteByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public long countByRadarUserId(Long radarUserId) {
    return this.licenseRepository.countByRadarUserId(radarUserId);
  }

  @Override
  @Transactional
  public void seed(Long radarUserId) throws Exception {
    // Read license_blips
    URL url = ResourceUtils.getURL("classpath:database/datasets/licenses_en.csv");
    String fileContent = new BufferedReader(new InputStreamReader(url.openStream())).lines()
        .collect(Collectors.joining("\n"));

    String[] record = null;
    final RadarUser radarUser = new RadarUser(radarUserId);
    CSVReader csvReader = new CSVReaderBuilder(new StringReader(fileContent))
        .withCSVParser(new CSVParserBuilder().withSeparator('|').build())
        .withSkipLines(1).build();
    while ((record = csvReader.readNext()) != null) {
      License license = new License();
      license.setRadarUser(radarUser);
      license.setTitle(record[0]);
      license.setDescription(record[1]);

      // Create only if not exists
      if (this.licenseRepository.findByRadarUserIdAndTitle(radarUserId, license.getTitle()).isEmpty()) {
        this.licenseRepository.save(license);
      }
    }
  }
}
