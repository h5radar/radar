package com.h5radar.radar.practice;

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



class PracticeServiceTests extends AbstractServiceTests {
  @MockitoBean
  private RadarUserRepository radarUserRepository;

  @MockitoBean
  private PracticeRepository practiceRepository;

  @Autowired
  private PracticeMapper practiceMapper;

  @Autowired
  private PracticeService practiceService;

  @Test
  void shouldFindAllPractices() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(1L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setRadarUser(radarUser);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    List<Practice> practiceList = List.of(practice);
    Mockito.when(practiceRepository.findAll(any(Sort.class))).thenReturn(practiceList);

    Collection<PracticeDto> practiceDtoCollection = practiceService.findAll();
    Assertions.assertEquals(1, practiceDtoCollection.size());
    Assertions.assertEquals(practiceDtoCollection.iterator().next().getId(), practice.getId());
    Assertions.assertEquals(practiceDtoCollection.iterator().next().getTitle(), practice.getTitle());
    Assertions.assertEquals(practiceDtoCollection.iterator().next().getDescription(), practice.getDescription());
  }

  @Test
  void shouldFindAllPracticesWithNullFilter() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    List<Practice> practiceList = List.of(practice);
    Page<Practice> page = new PageImpl<>(practiceList);
    Mockito.when(practiceRepository.findAll(ArgumentMatchers.<Specification<Practice>>any(), any(Pageable.class)))
        .thenReturn(page);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<PracticeDto> practiceDtoPage = practiceService.findAll(null, pageable);
    // Mockito.verify(practiceRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, practiceDtoPage.getSize());
    Assertions.assertEquals(0, practiceDtoPage.getNumber());
    Assertions.assertEquals(1, practiceDtoPage.getTotalPages());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getId(), practice.getId());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getTitle(), practice.getTitle());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getDescription(), practice.getDescription());

  }

  @Test
  void shouldFindAllPracticesWithEmptyFilter() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    List<Practice> practiceList = List.of(practice);
    Page<Practice> page = new PageImpl<>(practiceList);
    Mockito.when(practiceRepository.findAll(ArgumentMatchers.<Specification<Practice>>any(), any(Pageable.class)))
        .thenReturn(page);

    PracticeFilter practiceFilter = new PracticeFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("title,asc"));
    Page<PracticeDto> practiceDtoPage = practiceService.findAll(practiceFilter, pageable);
    // Mockito.verify(practiceRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, practiceDtoPage.getSize());
    Assertions.assertEquals(0, practiceDtoPage.getNumber());
    Assertions.assertEquals(1, practiceDtoPage.getTotalPages());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getId(), practice.getId());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getTitle(), practice.getTitle());
    Assertions.assertEquals(practiceDtoPage.iterator().next().getDescription(), practice.getDescription());

  }

  @Test
  void shouldFindByIdPractice() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    Mockito.when(practiceRepository.findById(practice.getId())).thenReturn(Optional.of(practice));
    Optional<PracticeDto> practiceDtoOptional = practiceService.findById(practice.getId());
    Mockito.verify(practiceRepository).findById(practice.getId());

    Assertions.assertTrue(practiceDtoOptional.isPresent());
    Assertions.assertEquals(practice.getId(), practiceDtoOptional.get().getId());
    Assertions.assertEquals(practice.getTitle(), practiceDtoOptional.get().getTitle());
    Assertions.assertEquals(practice.getDescription(), practiceDtoOptional.get().getDescription());

  }

  @Test
  void shouldFindByTitlePractice() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    Mockito.when(practiceRepository.findByTitle(practice.getTitle())).thenReturn(Optional.of(practice));
    Optional<PracticeDto> practiceDtoOptional = practiceService.findByTitle(practice.getTitle());
    Mockito.verify(practiceRepository).findByTitle(practice.getTitle());

    Assertions.assertTrue(practiceDtoOptional.isPresent());
    Assertions.assertEquals(practice.getId(), practiceDtoOptional.get().getId());
    Assertions.assertEquals(practice.getTitle(), practiceDtoOptional.get().getTitle());
    Assertions.assertEquals(practice.getDescription(), practiceDtoOptional.get().getDescription());

  }

  @Test
  void shouldSavePractice() {
    final RadarUser radarUser = new RadarUser();
    radarUser.setId(3L);
    radarUser.setSub("My sub");
    radarUser.setUsername("My username");

    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setRadarUser(radarUser);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    Mockito.when(radarUserRepository.findById(any())).thenReturn(Optional.of(radarUser));
    Mockito.when(practiceRepository.save(any())).thenReturn(practice);
    PracticeDto practiceDto = practiceService.save(practiceMapper.toDto(practice));
    Mockito.verify(radarUserRepository).findById(radarUser.getId());
    Mockito.verify(practiceRepository).save(any());

    Assertions.assertEquals(practice.getId(), practiceDto.getId());
    Assertions.assertEquals(practice.getTitle(), practiceDto.getTitle());
    Assertions.assertEquals(practice.getDescription(), practiceDto.getDescription());

  }

  @Test
  void shouldFailToSavePracticeDueToTitleWithWhiteSpace() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle(" My practice ");
    practice.setDescription("My practice description");
    practice.setActive(true);

    ValidationException exception = catchThrowableOfType(() ->
        practiceService.save(practiceMapper.toDto(practice)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeletePractice() {
    final Practice practice = new Practice();
    practice.setId(10L);
    practice.setTitle("My practice");
    practice.setDescription("My practice description");
    practice.setActive(true);

    Mockito.doAnswer((i) -> null).when(practiceRepository).deleteById(practice.getId());
    practiceService.deleteById(practice.getId());
    Mockito.verify(practiceRepository).deleteById(practice.getId());
  }
}
