package com.h5radar.radar.domain;

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
import com.h5radar.radar.radar_user.RadarUserDto;


@RestController
@Tag(name = "Domain API")
@RequestMapping("/api/v1/domains")
@RequiredArgsConstructor
public class DomainController {

  private static final String DOMAINS_TITLE_CONSTRAINTS = "uc_domains_radar_user_id_title";

  private final DomainService domainService;

  @GetMapping("")
  public ResponseEntity<Page<DomainDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid DomainFilter domainFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<DomainDto> domainDtoPage =
        domainService.findAll(domainFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(domainDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<DomainDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<DomainDto> domainRecord = domainService.findById(id);
    if (domainRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(domainRecord.get());
  }

  @PostMapping
  public ResponseEntity<DomainDto> create(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @RequestBody DomainDto domainDto) {
    domainDto.setId(null);
    domainDto.setRadarUserDto(new RadarUserDto(radarUserId));
    domainDto = domainService.save(domainDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(domainDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<DomainDto> update(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id, @RequestBody DomainDto domainDto) {
    Optional<DomainDto> domainRecord = domainService.findById(id);
    if (domainRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    domainDto.setId(id);
    domainDto.setRadarUserDto(new RadarUserDto(radarUserId));
    domainService.save(domainDto);
    return ResponseEntity.status(HttpStatus.OK).body(domainDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<DomainDto> domainRecord = domainService.findById(id);
    if (domainRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    domainService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<DomainDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId
  ) {
    if (this.domainService.countByRadarUserId(radarUserId) == 0) {
      try {
        domainService.seed(radarUserId);
      } catch (DataIntegrityViolationException exception) {
        if (!exception.getMessage().toLowerCase().contains(DOMAINS_TITLE_CONSTRAINTS)) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
