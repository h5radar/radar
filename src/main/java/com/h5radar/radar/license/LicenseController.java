package com.h5radar.radar.license;

import jakarta.validation.Valid;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h5radar.radar.RadarConstants;

@RestController
@Tag(name = "License API")
@RequestMapping("/api/v1/licenses")
@RequiredArgsConstructor
public class LicenseController {

  private static final String LICENSES_TITLE_CONSTRAINTS = "uc_licenses_radar_user_id_title";

  private final LicenseService licenseService;

  @GetMapping("")
  public ResponseEntity<Page<LicenseDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid LicenseFilter licenseFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<LicenseDto> licenseDtoPage =
        licenseService.findAll(licenseFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(licenseDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<LicenseDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<LicenseDto> licenseRecord = licenseService.findById(id);
    if (licenseRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(licenseRecord.get());
  }

  @PostMapping
  public ResponseEntity<LicenseDto> create(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @RequestBody LicenseDto licenseDto) {
    licenseDto.setId(null);
    licenseDto.setRadarUserId(radarUserId);
    licenseDto = licenseService.save(licenseDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(licenseDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<LicenseDto> update(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id, @RequestBody LicenseDto licenseDto) {
    Optional<LicenseDto> licenseRecord = licenseService.findById(id);
    if (licenseRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    licenseDto.setId(id);
    licenseDto.setRadarUserId(radarUserId);
    licenseService.save(licenseDto);
    return ResponseEntity.status(HttpStatus.OK).body(licenseDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<LicenseDto> licenseRecord = licenseService.findById(id);
    if (licenseRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    licenseService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<LicenseDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId
  ) {
    if (this.licenseService.countByRadarUserId(radarUserId) == 0) {
      try {
        licenseService.seed(radarUserId);
      } catch (DataIntegrityViolationException exception) {
        if (!exception.getMessage().toLowerCase().contains(LICENSES_TITLE_CONSTRAINTS)) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
