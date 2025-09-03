package com.h5radar.radar.practice;

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
@Tag(name = "Practice API")
@RequestMapping("/api/v1/practices")
@RequiredArgsConstructor
public class PracticeController {

  private static final String PRACTICES_TITLE_CONSTRAINTS = "uc_practices_radar_user_id_title";

  private final PracticeService practiceService;

  @GetMapping("")
  public ResponseEntity<Page<PracticeDto>> index(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid PracticeFilter practiceFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<PracticeDto> practiceDtoPage =
        practiceService.findAll(practiceFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(practiceDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<PracticeDto> show(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(practiceRecord.get());
  }

  @PostMapping
  public ResponseEntity<PracticeDto> create(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @RequestBody PracticeDto practiceDto) {
    practiceDto.setId(null);
    practiceDto.setRadarUserDto(new RadarUserDto(radarUserId));
    practiceDto = practiceService.save(practiceDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(practiceDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<PracticeDto> update(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id, @RequestBody PracticeDto practiceDto) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    practiceDto.setId(id);
    practiceDto.setRadarUserDto(new RadarUserDto(radarUserId));
    practiceService.save(practiceDto);
    return ResponseEntity.status(HttpStatus.OK).body(practiceDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    practiceService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<PracticeDto> seed(
      @RequestAttribute(RadarConstants.RADAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId) {
    if (this.practiceService.countByRadarUserId(radarUserId) == 0) {
      try {
        practiceService.seed(radarUserId);
      } catch (DataIntegrityViolationException exception) {
        if (!exception.getMessage().toLowerCase().contains(PRACTICES_TITLE_CONSTRAINTS)) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

}
