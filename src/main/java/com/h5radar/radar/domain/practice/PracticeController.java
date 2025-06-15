package com.h5radar.radar.domain.practice;

import jakarta.validation.Valid;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Practice API")
@RequestMapping("/api/v1/practices")
@RequiredArgsConstructor
public class PracticeController {

  private final PracticeService practiceService;

  @GetMapping("")
  public ResponseEntity<Page<PracticeDto>> index(
      @AuthenticationPrincipal Jwt jwt,
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
  public ResponseEntity<PracticeDto> show(@PathVariable("id") Long id) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(practiceRecord.get());
  }

  @PostMapping
  public ResponseEntity<PracticeDto> create(@RequestBody PracticeDto practiceDto) {
    practiceDto.setId(null);
    practiceDto = practiceService.save(practiceDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(practiceDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<PracticeDto> update(@PathVariable("id") Long id, @RequestBody PracticeDto practiceDto) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    practiceDto.setId(id);
    practiceService.save(practiceDto);
    return ResponseEntity.status(HttpStatus.OK).body(practiceDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    Optional<PracticeDto> practiceRecord = practiceService.findById(id);
    if (practiceRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    practiceService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/seed")
  public ResponseEntity<PracticeDto> seed() {
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

}
