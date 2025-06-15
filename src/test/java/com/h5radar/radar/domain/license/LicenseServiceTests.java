package com.h5radar.radar.domain.license;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractServiceTests;
import com.h5radar.radar.domain.ValidationException;
import com.h5radar.radar.domain.radar_user.RadarUser;
import com.h5radar.radar.domain.radar_user.RadarUserRepository;



class LicenseServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository radarUserRepository;
  @MockitoBean
  private LicenseRepository licenseRepository;
  @Autowired
  private LicenseMapper licenseMapper;
  @Autowired
  private LicenseService licenseService;

  @Test
  void shouldFindAllLicenses() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final License license = new License();
    license.setId(10L);
    license.setRadarUser(radarUser);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    List<License> licenseList = List.of(license);
    Mockito.when(licenseRepository.findAll(any(Sort.class))).thenReturn(licenseList);

    Collection<LicenseDto> licenseDtoCollection = licenseService.findAll();
    Assertions.assertEquals(1, licenseDtoCollection.size());
    Assertions.assertEquals(licenseDtoCollection.iterator().next().getId(), license.getId());
    Assertions.assertEquals(licenseDtoCollection.iterator().next().getTitle(), license.getTitle());
    Assertions.assertEquals(licenseDtoCollection.iterator().next().getDescription(), license.getDescription());
  }

  @Test
  void shouldFindAllLicensesWithEmptyFilter() {
    final License license = new License();
    license.setId(10L);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    List<License> licenseList = List.of(license);
    Page<License> page = new PageImpl<>(licenseList);
    Mockito.when(licenseRepository.findAll(ArgumentMatchers.<Specification<License>>any(), any(Pageable.class)))
        .thenReturn(page);

    LicenseFilter licenseFilter = new LicenseFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<LicenseDto> licenseDtoPage = licenseService.findAll(licenseFilter, pageable);
    Assertions.assertEquals(1, licenseDtoPage.getSize());
    Assertions.assertEquals(0, licenseDtoPage.getNumber());
    Assertions.assertEquals(1, licenseDtoPage.getTotalPages());
    Assertions.assertEquals(licenseDtoPage.iterator().next().getId(), license.getId());
    Assertions.assertEquals(licenseDtoPage.iterator().next().getTitle(), license.getTitle());
    Assertions.assertEquals(licenseDtoPage.iterator().next().getDescription(), license.getDescription());

    // Mockito.verify(licenseRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);
  }

  @Test
  void shouldFindByIdLicense() {
    final License license = new License();
    license.setId(10L);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    Mockito.when(licenseRepository.findById(license.getId())).thenReturn(Optional.of(license));

    Optional<LicenseDto> licenseDtoOptional = licenseService.findById(license.getId());
    Assertions.assertTrue(licenseDtoOptional.isPresent());
    Assertions.assertEquals(license.getId(), licenseDtoOptional.get().getId());
    Assertions.assertEquals(license.getTitle(), licenseDtoOptional.get().getTitle());
    Assertions.assertEquals(license.getDescription(), licenseDtoOptional.get().getDescription());

    Mockito.verify(licenseRepository).findById(license.getId());
  }

  @Test
  void shouldFindByTitleLicense() {
    final License license = new License();
    license.setId(10L);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    Mockito.when(licenseRepository.findByTitle(license.getTitle())).thenReturn(Optional.of(license));

    Optional<LicenseDto> licenseDtoOptional = licenseService.findByTitle(license.getTitle());
    Assertions.assertTrue(licenseDtoOptional.isPresent());
    Assertions.assertEquals(license.getId(), licenseDtoOptional.get().getId());
    Assertions.assertEquals(license.getTitle(), licenseDtoOptional.get().getTitle());
    Assertions.assertEquals(license.getDescription(), licenseDtoOptional.get().getDescription());

    Mockito.verify(licenseRepository).findByTitle(license.getTitle());
  }

  @Test
  void shouldSaveLicense() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final License license = new License();
    license.setId(10L);
    license.setRadarUser(radarUser);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    Mockito.when(radarUserRepository.findById(any())).thenReturn(Optional.of(radarUser));
    Mockito.when(licenseRepository.save(any())).thenReturn(license);

    LicenseDto licenseDto = licenseService.save(licenseMapper.toDto(license));
    Assertions.assertEquals(license.getId(), licenseDto.getId());
    Assertions.assertEquals(license.getTitle(), licenseDto.getTitle());
    Assertions.assertEquals(license.getDescription(), licenseDto.getDescription());

    Mockito.verify(radarUserRepository).findById(radarUser.getId());
    Mockito.verify(licenseRepository).save(any());
  }

  @Test
  void shouldFailToSaveLicenseDueToTitleWithWhiteSpace() {
    final License license = new License();
    license.setId(10L);
    license.setTitle(" My license ");
    license.setDescription("My license description");
    license.setActive(true);

    ValidationException exception = catchThrowableOfType(() ->
        licenseService.save(licenseMapper.toDto(license)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteLicense() {
    final License license = new License();
    license.setId(10L);
    license.setTitle("My license");
    license.setDescription("My license description");
    license.setActive(true);

    Mockito.doAnswer((i) -> null).when(licenseRepository).deleteById(license.getId());

    licenseService.deleteById(license.getId());
    Mockito.verify(licenseRepository).deleteById(license.getId());
  }
}
