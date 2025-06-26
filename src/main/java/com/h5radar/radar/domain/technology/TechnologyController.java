package com.h5radar.radar.domain.technology;

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
@Tag(name = "Technology API")
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
public class TechnologyController {

  private static final String TECHNOLOGIES_TITLE_CONSTRAINTS = "uc_technologies_radar_user_id_title";

  private final TechnologyService technologyService;

  @GetMapping("")
  public ResponseEntity<Page<TechnologyDto>> index(
      @RequestAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME) String radarUserId,
      @Valid TechnologyFilter technologyFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "title,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<TechnologyDto> technologyDtoPage =
        technologyService.findAll(technologyFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(technologyDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<TechnologyDto> show(@PathVariable("id") Long id) {
    Optional<TechnologyDto> technologyRecord = technologyService.findById(id);
    if (technologyRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(technologyRecord.get());
  }

  @PostMapping
  public ResponseEntity<TechnologyDto> create(@RequestBody TechnologyDto technologyDto) {
    technologyDto.setId(null);
    technologyDto = technologyService.save(technologyDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(technologyDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<TechnologyDto> update(@PathVariable("id") Long id, @RequestBody TechnologyDto technologyDto) {
    Optional<TechnologyDto> technologyRecord = technologyService.findById(id);
    if (technologyRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    technologyDto.setId(id);
    technologyService.save(technologyDto);
    return ResponseEntity.status(HttpStatus.OK).body(technologyDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    Optional<TechnologyDto> technologyRecord = technologyService.findById(id);
    if (technologyRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    technologyService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<TechnologyDto> seed(
      @RequestAttribute(RadarConstants.RARDAR_USER_ID_ATTRIBUTE_NAME) Long radarUserId) {
    try {
      technologyService.seed(radarUserId);
    } catch (DataIntegrityViolationException exception) {
      if (!exception.getMessage().toLowerCase().contains(TECHNOLOGIES_TITLE_CONSTRAINTS)) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
