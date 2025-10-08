package com.h5radar.radar.radar_user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.h5radar.radar.AbstractIntegrationTests;
import com.h5radar.radar.compliance.ComplianceService;
import com.h5radar.radar.domain.DomainService;
import com.h5radar.radar.license.LicenseService;
import com.h5radar.radar.maturity.MaturityService;
import com.h5radar.radar.practice.PracticeService;
import com.h5radar.radar.technology.TechnologyService;


class RadarUserIntegrationTests extends AbstractIntegrationTests {

  @Autowired
  private RadarUserService radarUserService;

  @Autowired
  private ComplianceService complianceService;

  @Autowired
  private LicenseService licenseService;

  @Autowired private
  PracticeService practiceService;

  @Autowired
  private MaturityService maturityService;

  @Autowired
  private DomainService domainService;

  @Autowired
  private TechnologyService technologyService;

  @Test
  public void shouldGetRadarUsers() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.get().uri("/api/v1/radar-users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(radarUserDto.getId())
        .jsonPath("$.content[0].sub").isEqualTo(radarUserDto.getSub())
        .jsonPath("$.content[0].username").isEqualTo(radarUserDto.getUsername())
        .jsonPath("$.content[0].seeded").isEqualTo(false)
        .jsonPath("$.content[0].seededDate").doesNotExist();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  public void shouldGetRadarUser() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);

    webTestClient.get().uri("/api/v1/radar-users/{id}", radarUserDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(radarUserDto.getId())
        .jsonPath("$.sub").isEqualTo(radarUserDto.getSub())
        .jsonPath("$.username").isEqualTo(radarUserDto.getUsername())
        .jsonPath("$.seeded").isEqualTo(false)
        .jsonPath("$.seededDate").doesNotExist();

    radarUserService.deleteById(radarUserDto.getId());
  }

  @Test
  void shouldSeedUserDataOnce() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);
    Long radarUserId = radarUserDto.getId();

    // Ensure empty before seed
    assert complianceService.countByRadarUserId(radarUserId) == 0;
    assert licenseService.countByRadarUserId(radarUserId) == 0;
    assert practiceService.countByRadarUserId(radarUserId) == 0;
    assert maturityService.countByRadarUserId(radarUserId) == 0;
    assert domainService.countByRadarUserId(radarUserId) == 0;
    assert technologyService.countByRadarUserId(radarUserId) == 0;

    // Call seed endpoint
    webTestClient.post().uri("/api/v1/radar-users/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    // Now data must be present (>= 1)
    assert complianceService.countByRadarUserId(radarUserId) > 0;
    assert licenseService.countByRadarUserId(radarUserId) > 0;
    assert practiceService.countByRadarUserId(radarUserId) > 0;
    assert maturityService.countByRadarUserId(radarUserId) > 0;
    assert domainService.countByRadarUserId(radarUserId) > 0;
    assert technologyService.countByRadarUserId(radarUserId) > 0;

    radarUserService.deleteById(radarUserId);
  }

  @Test
  void shouldNotDuplicateOnSecondSeedCall() {
    // Create radarUser
    RadarUserDto radarUserDto = new RadarUserDto();
    radarUserDto.setId(null);
    radarUserDto.setSub("My sub");
    radarUserDto.setUsername("My username");
    radarUserDto = radarUserService.save(radarUserDto);
    Long radarUserId = radarUserDto.getId();

    // First seed
    webTestClient.post().uri("/api/v1/radar-users/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    final long c1 = complianceService.countByRadarUserId(radarUserId);
    final long l1 = licenseService.countByRadarUserId(radarUserId);
    final long p1 = practiceService.countByRadarUserId(radarUserId);
    final long m1 = maturityService.countByRadarUserId(radarUserId);
    final long d1 = domainService.countByRadarUserId(radarUserId);
    final long t1 = technologyService.countByRadarUserId(radarUserId);

    // Second seed (should be a no-op per controller's count==0 guard)
    webTestClient.post().uri("/api/v1/radar-users/seed")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    final long c2 = complianceService.countByRadarUserId(radarUserId);
    final long l2 = licenseService.countByRadarUserId(radarUserId);
    final long p2 = practiceService.countByRadarUserId(radarUserId);
    final long m2 = maturityService.countByRadarUserId(radarUserId);
    final long d2 = domainService.countByRadarUserId(radarUserId);
    final long t2 = technologyService.countByRadarUserId(radarUserId);

    assert c2 == c1;
    assert l2 == l1;
    assert p2 == p1;
    assert m2 == m1;
    assert d2 == d1;
    assert t2 == t1;

    radarUserService.deleteById(radarUserId);
  }
}
