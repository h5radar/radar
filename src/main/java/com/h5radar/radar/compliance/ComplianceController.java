package com.h5radar.radar.compliance;

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
@Tag(name = "Compliance API")
@RequestMapping("/api/v1/compliances")
@RequiredArgsConstructor
public class ComplianceController {

  private static final String COMPLIANCE_TITLE_CONSTRAINTS = "uc_compliances_radar_user_id_title";

  private final ComplianceService complianceService;

  @GetMapping("")
  public ResponseEntity<Page<ComplianceDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid ComplianceFilter complianceFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<ComplianceDto> complianceDtoPage =
        complianceService.findAll(complianceFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(complianceDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ComplianceDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<ComplianceDto> complianceRecord = complianceService.findById(id);
    if (complianceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(complianceRecord.get());
  }

  @PostMapping
  public ResponseEntity<ComplianceDto> create(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @RequestBody ComplianceDto complianceDto) {
    complianceDto.setId(null);
    complianceDto.setRadarUserId(radarUserId);
    complianceDto = complianceService.save(complianceDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(complianceDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ComplianceDto> update(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id, @RequestBody ComplianceDto complianceDto) {
    Optional<ComplianceDto> complianceRecord = complianceService.findById(id);
    if (complianceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    complianceDto.setId(id);
    complianceDto.setRadarUserId(radarUserId);
    complianceService.save(complianceDto);
    return ResponseEntity.status(HttpStatus.OK).body(complianceDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<ComplianceDto> complianceRecord = complianceService.findById(id);
    if (complianceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    complianceService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<ComplianceDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId
  ) {
    if (this.complianceService.countByRadarUserId(radarUserId) == 0) {
      try {
        complianceService.seed(radarUserId);
      } catch (DataIntegrityViolationException exception) {
        if (!exception.getMessage().toLowerCase().contains(COMPLIANCE_TITLE_CONSTRAINTS)) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
