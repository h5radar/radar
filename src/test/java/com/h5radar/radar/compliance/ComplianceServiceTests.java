package com.h5radar.radar.compliance;

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

import com.h5radar.radar.AbstractServiceTests;
import com.h5radar.radar.ValidationException;
import com.h5radar.radar.radar_user.RadarUser;
import com.h5radar.radar.radar_user.RadarUserRepository;



class ComplianceServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository radarUserRepository;
  @MockitoBean
  private ComplianceRepository complianceRepository;
  @Autowired
  private ComplianceMapper complianceMapper;
  @Autowired
  private ComplianceService complianceService;

  @Test
  void shouldFindAllCompliances() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    List<Compliance> complianceList = List.of(compliance);
    Mockito.when(complianceRepository.findAll(any(Sort.class))).thenReturn(complianceList);

    Collection<ComplianceDto> complianceDtoCollection = complianceService.findAll();
    Assertions.assertEquals(1, complianceDtoCollection.size());
    Assertions.assertEquals(complianceDtoCollection.iterator().next().getId(), compliance.getId());
    Assertions.assertEquals(complianceDtoCollection.iterator().next().getTitle(), compliance.getTitle());
    Assertions.assertEquals(complianceDtoCollection.iterator().next().getDescription(), compliance.getDescription());
  }

  @Test
  void shouldFindAllCompliancesWithNullFilter() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    List<Compliance> complianceList = List.of(compliance);
    Page<Compliance> page = new PageImpl<>(complianceList);
    Mockito.when(complianceRepository.findAll(ArgumentMatchers.<Specification<Compliance>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<ComplianceDto> complianceDtoPage = complianceService.findAll(null, pageable);
    // Mockito.verify(complianceRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, complianceDtoPage.getSize());
    Assertions.assertEquals(0, complianceDtoPage.getNumber());
    Assertions.assertEquals(1, complianceDtoPage.getTotalPages());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getId(), compliance.getId());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getTitle(), compliance.getTitle());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getDescription(), compliance.getDescription());
  }

  @Test
  void shouldFindAllCompliancesWithEmptyFilter() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    List<Compliance> complianceList = List.of(compliance);
    Page<Compliance> page = new PageImpl<>(complianceList);
    Mockito.when(complianceRepository.findAll(ArgumentMatchers.<Specification<Compliance>>any(), any(Pageable.class)))
        .thenReturn(page);

    ComplianceFilter complianceFilter = new ComplianceFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<ComplianceDto> complianceDtoPage = complianceService.findAll(complianceFilter, pageable);
    // Mockito.verify(complianceRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, complianceDtoPage.getSize());
    Assertions.assertEquals(0, complianceDtoPage.getNumber());
    Assertions.assertEquals(1, complianceDtoPage.getTotalPages());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getId(), compliance.getId());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getTitle(), compliance.getTitle());
    Assertions.assertEquals(complianceDtoPage.iterator().next().getDescription(), compliance.getDescription());
  }

  @Test
  void shouldFindByIdCompliance() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    Mockito.when(complianceRepository.findById(compliance.getId())).thenReturn(Optional.of(compliance));
    Optional<ComplianceDto> complianceDtoOptional = complianceService.findById(compliance.getId());
    Mockito.verify(complianceRepository).findById(compliance.getId());

    Assertions.assertTrue(complianceDtoOptional.isPresent());
    Assertions.assertEquals(compliance.getId(), complianceDtoOptional.get().getId());
    Assertions.assertEquals(compliance.getTitle(), complianceDtoOptional.get().getTitle());
    Assertions.assertEquals(compliance.getDescription(), complianceDtoOptional.get().getDescription());

  }

  @Test
  void shouldFindByTitleCompliance() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    Mockito.when(complianceRepository.findByTitle(compliance.getTitle())).thenReturn(Optional.of(compliance));
    Optional<ComplianceDto> complianceDtoOptional = complianceService.findByTitle(compliance.getTitle());
    Mockito.verify(complianceRepository).findByTitle(compliance.getTitle());

    Assertions.assertTrue(complianceDtoOptional.isPresent());
    Assertions.assertEquals(compliance.getId(), complianceDtoOptional.get().getId());
    Assertions.assertEquals(compliance.getTitle(), complianceDtoOptional.get().getTitle());
    Assertions.assertEquals(compliance.getDescription(), complianceDtoOptional.get().getDescription());

  }

  @Test
  void shouldSaveCompliance() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setRadarUser(radarUser);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    Mockito.when(radarUserRepository.findById(any())).thenReturn(Optional.of(radarUser));
    Mockito.when(complianceRepository.save(any())).thenReturn(compliance);
    ComplianceDto complianceDto = complianceService.save(complianceMapper.toDto(compliance));
    Mockito.verify(radarUserRepository).findById(radarUser.getId());
    Mockito.verify(complianceRepository).save(any());

    Assertions.assertEquals(compliance.getId(), complianceDto.getId());
    Assertions.assertEquals(compliance.getTitle(), complianceDto.getTitle());
    Assertions.assertEquals(compliance.getDescription(), complianceDto.getDescription());
  }

  @Test
  void shouldFailToSaveComplianceDueToTitleWithWhiteSpace() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle(" My compliance ");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    ValidationException exception = catchThrowableOfType(() ->
        complianceService.save(complianceMapper.toDto(compliance)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteCompliance() {
    final Compliance compliance = new Compliance();
    compliance.setId(10L);
    compliance.setTitle("My compliance");
    compliance.setDescription("My compliance description");
    compliance.setActive(true);

    Mockito.doAnswer((i) -> null).when(complianceRepository).deleteById(compliance.getId());
    complianceService.deleteById(compliance.getId());
    Mockito.verify(complianceRepository).deleteById(compliance.getId());
  }
}
