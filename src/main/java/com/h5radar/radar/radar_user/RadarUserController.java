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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "RadarUser API")
@RequestMapping("/api/v1/radar-users")
@RequiredArgsConstructor
public class RadarUserController {

  private static final String RADAR_USERS_SUB_CONSTRAINTS = "uc_radar_users_sub";

  private final RadarUserService radarUserService;

  @GetMapping("")
  public ResponseEntity<Page<RadarUserDto>> index(
      @AuthenticationPrincipal Jwt jwt,
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
  public ResponseEntity<RadarUserDto> show(@PathVariable("id") Long id) {
    Optional<RadarUserDto> radarUserRecord = radarUserService.findById(id);
    if (radarUserRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(radarUserRecord.get());
  }
}
