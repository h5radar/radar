package com.h5radar.radar.maturity;

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
@Tag(name = "Maturity API")
@RequestMapping("/api/v1/maturities")
@RequiredArgsConstructor
public class MaturityController {

  private static final String MATURITIES_TITLE_CONSTRAINTS = "uc_maturities_radar_user_id_title";

  private final MaturityService maturityService;

  @GetMapping("")
  public ResponseEntity<Page<MaturityDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid MaturityFilter maturityFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<MaturityDto> maturityDtoPage =
        maturityService.findAll(maturityFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(maturityDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<MaturityDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<MaturityDto> maturityRecord = maturityService.findById(id);
    if (maturityRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(maturityRecord.get());
  }

  @PostMapping
  public ResponseEntity<MaturityDto> create(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @RequestBody MaturityDto maturityDto) {
    maturityDto.setId(null);
    maturityDto.setRadarUserDto(new RadarUserDto(radarUserId));
    maturityDto = maturityService.save(maturityDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(maturityDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<MaturityDto> update(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id, @RequestBody MaturityDto maturityDto) {
    Optional<MaturityDto> maturityRecord = maturityService.findById(id);
    if (maturityRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    maturityDto.setId(id);
    maturityDto.setRadarUserDto(new RadarUserDto(radarUserId));
    maturityService.save(maturityDto);
    return ResponseEntity.status(HttpStatus.OK).body(maturityDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<MaturityDto> maturityRecord = maturityService.findById(id);
    if (maturityRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    maturityService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<MaturityDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId
  ) {
    if (this.maturityService.countByRadarUserId(radarUserId) == 0) {
      try {
        maturityService.seed(radarUserId);
      } catch (DataIntegrityViolationException exception) {
        if (!exception.getMessage().toLowerCase().contains(MATURITIES_TITLE_CONSTRAINTS)) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
