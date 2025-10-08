package com.h5radar.radar.radar_user;

import jakarta.validation.Valid;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h5radar.radar.RadarConstants;
import com.h5radar.radar.compliance.ComplianceService;
import com.h5radar.radar.domain.DomainService;
import com.h5radar.radar.license.LicenseService;
import com.h5radar.radar.maturity.MaturityService;
import com.h5radar.radar.practice.PracticeService;
import com.h5radar.radar.technology.TechnologyService;


@RestController
@Tag(name = "RadarUser API")
@RequestMapping("/api/v1/radar-users")
@RequiredArgsConstructor
public class RadarUserController {

  private final RadarUserService radarUserService;
  private final ComplianceService complianceService;
  private final LicenseService licenseService;
  private final PracticeService practiceService;
  private final MaturityService maturityService;
  private final DomainService domainService;
  private final TechnologyService technologyService;

  @GetMapping("")
  public ResponseEntity<Page<RadarUserDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid RadarUserFilter radarUserFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "sub,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<RadarUserDto> radarUserDtoPage =
        radarUserService.findAll(radarUserFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(radarUserDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<RadarUserDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<RadarUserDto> radarUserRecord = radarUserService.findById(radarUserId);
    if (radarUserRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(radarUserRecord.get());
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<RadarUserDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId
  ) {
    try {
      // Seed entities if necessary
      if (this.complianceService.countByRadarUserId(radarUserId) == 0
          && this.licenseService.countByRadarUserId(radarUserId) == 0
          && this.practiceService.countByRadarUserId(radarUserId) == 0
          && this.maturityService.countByRadarUserId(radarUserId) == 0
          && this.domainService.countByRadarUserId(radarUserId) == 0
          && this.technologyService.countByRadarUserId(radarUserId) == 0) {

        this.complianceService.seed(radarUserId);
        this.licenseService.seed(radarUserId);
        this.practiceService.seed(radarUserId);
        this.maturityService.seed(radarUserId);
        this.domainService.seed(radarUserId);
        this.technologyService.seed(radarUserId);

        // Update seeded and seeded date
        this.radarUserService.updateSeed(radarUserId);
      }

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}

