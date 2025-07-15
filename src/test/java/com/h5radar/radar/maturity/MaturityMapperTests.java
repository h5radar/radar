package com.h5radar.radar.maturity;

import static org.mockito.ArgumentMatchers.any;

import com.h5radar.radar.AbstractMapperTests;

class MaturityMapperTests extends AbstractMapperTests {
  /* TODO: uncomment

  @MockitoBean
  private RadarRepository radarRepository;

  @MockitoBean
  private TechnologyBlipMapper technologyBlipMapper;

  @Autowired
  private MaturityMapper maturityMapper;

  @Test
  void testToDtoWithNull() {
    final var maturityDto = maturityMapper.toDto(null);

    Assertions.assertNull(maturityDto);
  }

  @Test
  void testToDtoAllFields() {
    final Maturity maturity = new Maturity();
    maturity.setId(0L);
    maturity.setTitle("My maturity title");
    maturity.setDescription("My maturity description");
    maturity.setColor("color");
    maturity.setPosition(1);

    final var maturityDto = maturityMapper.toDto(maturity);

    Assertions.assertEquals(maturityDto.getTitle(), maturity.getTitle());
    Assertions.assertEquals(maturityDto.getDescription(), maturity.getDescription());
    Assertions.assertEquals(maturityDto.getColor(), maturity.getColor());
    Assertions.assertEquals(maturityDto.getPosition(), maturity.getPosition());
  }

  @Test
  public void testToDtoAllLists() {
    final RadarType radarType = new RadarType();
    radarType.setId(10L);
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarType.setTitle("My title");
    radarType.setDescription("My description");

    // Create radar
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(radarType);
    radar.setTitle("My title");
    radar.setDescription("My description");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setMaturityList(List.of(new Maturity()));
    radar.setTechnologyBlipList(List.of(new TechnologyBlip()));

    // Create maturity
    final Maturity maturity = new Maturity();
    maturity.setId(2L);
    maturity.setRadar(radar);
    maturity.setTitle("My title");
    maturity.setDescription("My description");
    maturity.setColor("My color");
    maturity.setPosition(1);
    maturity.setTechnologyBlipList(List.of(new TechnologyBlip()));

    // Create segment
    final var segmentDto = new SegmentDto();
    segmentDto.setId(3L);
    segmentDto.setTitle("My segment title");
    segmentDto.setDescription("My segment description");
    segmentDto.setPosition(2);

    // Create technology
    final var technologyDto = new TechnologyDto();
    technologyDto.setId(4L);
    technologyDto.setTitle("My technology title");
    technologyDto.setWebsite("https://www.example.com");
    technologyDto.setDescription("My technology description");
    technologyDto.setMoved(1);
    technologyDto.setActive(true);

    // Create technologyBlip
    final var technologyBlipDto = new TechnologyBlipDto();
    technologyBlipDto.setId(5L);
    technologyBlipDto.setRadarId(radar.getId());
    technologyBlipDto.setMaturityId(maturity.getId());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setSegmentId(segmentDto.getId());

    Mockito.when(technologyBlipMapper.toDto(any())).thenReturn(technologyBlipDto);

    MaturityDto maturityDto = maturityMapper.toDto(maturity);

    Assertions.assertEquals(maturity.getId(), maturityDto.getId());
    Assertions.assertEquals(maturity.getTitle(), maturityDto.getTitle());
    Assertions.assertEquals(maturity.getDescription(), maturityDto.getDescription());
    Assertions.assertEquals(maturity.getColor(), maturityDto.getColor());
    Assertions.assertEquals(maturity.getPosition(), maturityDto.getPosition());

    Assertions.assertEquals(maturity.getRadar().getId(), maturityDto.getRadarId());
    Assertions.assertEquals(maturity.getRadar().getTitle(), maturityDto.getRadarTitle());

    Assertions.assertNotNull(maturityDto.getTechnologyBlipDtoList());
    Assertions.assertEquals(1, maturityDto.getTechnologyBlipDtoList().size());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getId(),
      technologyBlipDto.getId());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getRadarId(),
        technologyBlipDto.getRadarId());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getRadarTitle(),
        technologyBlipDto.getRadarTitle());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getTechnologyId(),
        technologyBlipDto.getTechnologyId());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getTechnologyTitle(),
        technologyBlipDto.getTechnologyTitle());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getTechnologyWebsite(),
        technologyBlipDto.getTechnologyWebsite());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getTechnologyMoved(),
        technologyBlipDto.getTechnologyMoved());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().isTechnologyActive(),
        technologyBlipDto.isTechnologyActive());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getSegmentId(),
        technologyBlipDto.getSegmentId());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getSegmentTitle(),
        technologyBlipDto.getSegmentTitle());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getSegmentPosition(),
        technologyBlipDto.getSegmentPosition());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getMaturityId(),
        technologyBlipDto.getMaturityId());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getMaturityTitle(),
        technologyBlipDto.getMaturityTitle());
    Assertions.assertEquals(maturityDto.getTechnologyBlipDtoList().iterator().next().getMaturityPosition(),
        technologyBlipDto.getMaturityPosition());

    Mockito.verify(technologyBlipMapper).toDto(any());
  }

  @Test
  void testToEntityAllFields() {
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar Description");
    radar.setPrimary(true);
    radar.setActive(true);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(2L);
    maturityDto.setRadarId(radar.getId());
    maturityDto.setTitle("My maturity title1");
    maturityDto.setDescription("My maturity description1");
    maturityDto.setColor("color");
    maturityDto.setPosition(1);
    final var maturity = maturityMapper.toEntity(maturityDto);

    Assertions.assertEquals(maturity.getId(), maturityDto.getId());
    Assertions.assertEquals(maturity.getTitle(), maturityDto.getTitle());
    Assertions.assertEquals(maturity.getDescription(), maturityDto.getDescription());
    Assertions.assertEquals(maturityDto.getColor(), maturity.getColor());
    Assertions.assertEquals(maturityDto.getPosition(), maturity.getPosition());

    Mockito.verify(radarRepository).findById(radar.getId());
  }

  @Test
  public void testToEntityAllLists() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarType.setTitle("My radarType title");
    radarType.setDescription("My radarType description");

    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(true);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    final Maturity maturity = new Maturity();
    maturity.setId(2L);
    maturity.setRadar(radar);
    maturity.setTitle("My maturity title");
    maturity.setDescription("My maturity description");
    maturity.setColor("My color");
    maturity.setPosition(1);

    final MaturityDto maturityDto = new MaturityDto();
    maturityDto.setId(2L);
    maturityDto.setRadarId(radar.getId());
    maturityDto.setRadarTitle(radar.getTitle());
    maturityDto.setTitle("My maturity title");
    maturityDto.setDescription("My maturity description");
    maturityDto.setColor("color");
    maturityDto.setPosition(1);
    maturityDto.setTechnologyBlipDtoList(List.of(new TechnologyBlipDto()));

    final var technologyBlip = new TechnologyBlip();
    technologyBlip.setId(3L);
    technologyBlip.setRadar(radar);
    technologyBlip.setMaturity(maturity);

    Mockito.when(technologyBlipMapper.toEntity(any())).thenReturn(technologyBlip);

    final var mappedMaturity = maturityMapper.toEntity(maturityDto);

    Assertions.assertEquals(mappedMaturity.getId(), maturityDto.getId());
    Assertions.assertEquals(mappedMaturity.getTitle(), maturityDto.getTitle());
    Assertions.assertEquals(mappedMaturity.getDescription(), maturityDto.getDescription());
    Assertions.assertEquals(mappedMaturity.getColor(), maturityDto.getColor());
    Assertions.assertEquals(mappedMaturity.getPosition(), maturityDto.getPosition());
    Assertions.assertEquals(mappedMaturity.getId(),
        mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getId());
    Assertions.assertEquals(mappedMaturity.getTitle(),
        mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getTitle());
    Assertions.assertEquals(mappedMaturity.getDescription(),
        mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getDescription());
    Assertions.assertEquals(mappedMaturity.getPosition(),
        mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getPosition());
    Assertions.assertEquals(mappedMaturity.getRadar().getId(),
        mappedMaturity.getTechnologyBlipList().iterator().next().getRadar().getId());

    Assertions.assertEquals(mappedMaturity.getRadar().getId(), maturityDto.getRadarId());
    Assertions.assertEquals(mappedMaturity.getRadar().getTitle(), maturityDto.getRadarTitle());

    Assertions.assertNotNull(mappedMaturity.getTechnologyBlipList());
    Assertions.assertEquals(1, mappedMaturity.getTechnologyBlipList().size());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getId(), technologyBlip.getId());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getRadar().getId(),
        technologyBlip.getRadar().getId());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getId(),
        technologyBlip.getMaturity().getId());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getTitle(),
        technologyBlip.getMaturity().getTitle());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getDescription(),
        technologyBlip.getMaturity().getDescription());
    Assertions.assertEquals(mappedMaturity.getTechnologyBlipList().iterator().next().getMaturity().getPosition(),
        technologyBlip.getMaturity().getPosition());

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(technologyBlipMapper).toEntity(any());
  }
  */
}
