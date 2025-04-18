package com.h5radar.radar.domain.ring;

import static org.mockito.ArgumentMatchers.any;

import com.h5radar.radar.domain.AbstractMapperTests;

class RingMapperTests extends AbstractMapperTests {
  /* TODO: uncomment

  @MockitoBean
  private RadarRepository radarRepository;

  @MockitoBean
  private TechnologyBlipMapper technologyBlipMapper;

  @Autowired
  private RingMapper ringMapper;

  @Test
  void testToDtoWithNull() {
    final var ringDto = ringMapper.toDto(null);

    Assertions.assertNull(ringDto);
  }

  @Test
  void testToDtoAllFields() {
    final Ring ring = new Ring();
    ring.setId(0L);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setColor("color");
    ring.setPosition(1);

    final var ringDto = ringMapper.toDto(ring);

    Assertions.assertEquals(ringDto.getTitle(), ring.getTitle());
    Assertions.assertEquals(ringDto.getDescription(), ring.getDescription());
    Assertions.assertEquals(ringDto.getColor(), ring.getColor());
    Assertions.assertEquals(ringDto.getPosition(), ring.getPosition());
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
    radar.setRingList(List.of(new Ring()));
    radar.setTechnologyBlipList(List.of(new TechnologyBlip()));

    // Create ring
    final Ring ring = new Ring();
    ring.setId(2L);
    ring.setRadar(radar);
    ring.setTitle("My title");
    ring.setDescription("My description");
    ring.setColor("My color");
    ring.setPosition(1);
    ring.setTechnologyBlipList(List.of(new TechnologyBlip()));

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
    technologyBlipDto.setRingId(ring.getId());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setSegmentId(segmentDto.getId());

    Mockito.when(technologyBlipMapper.toDto(any())).thenReturn(technologyBlipDto);

    RingDto ringDto = ringMapper.toDto(ring);

    Assertions.assertEquals(ring.getId(), ringDto.getId());
    Assertions.assertEquals(ring.getTitle(), ringDto.getTitle());
    Assertions.assertEquals(ring.getDescription(), ringDto.getDescription());
    Assertions.assertEquals(ring.getColor(), ringDto.getColor());
    Assertions.assertEquals(ring.getPosition(), ringDto.getPosition());

    Assertions.assertEquals(ring.getRadar().getId(), ringDto.getRadarId());
    Assertions.assertEquals(ring.getRadar().getTitle(), ringDto.getRadarTitle());

    Assertions.assertNotNull(ringDto.getTechnologyBlipDtoList());
    Assertions.assertEquals(1, ringDto.getTechnologyBlipDtoList().size());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getId(), technologyBlipDto.getId());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getRadarId(),
        technologyBlipDto.getRadarId());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getRadarTitle(),
        technologyBlipDto.getRadarTitle());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getTechnologyId(),
        technologyBlipDto.getTechnologyId());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getTechnologyTitle(),
        technologyBlipDto.getTechnologyTitle());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getTechnologyWebsite(),
        technologyBlipDto.getTechnologyWebsite());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getTechnologyMoved(),
        technologyBlipDto.getTechnologyMoved());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().isTechnologyActive(),
        technologyBlipDto.isTechnologyActive());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getSegmentId(),
        technologyBlipDto.getSegmentId());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getSegmentTitle(),
        technologyBlipDto.getSegmentTitle());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getSegmentPosition(),
        technologyBlipDto.getSegmentPosition());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getRingId(),
        technologyBlipDto.getRingId());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getRingTitle(),
        technologyBlipDto.getRingTitle());
    Assertions.assertEquals(ringDto.getTechnologyBlipDtoList().iterator().next().getRingPosition(),
        technologyBlipDto.getRingPosition());

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

    final RingDto ringDto = new RingDto();
    ringDto.setId(2L);
    ringDto.setRadarId(radar.getId());
    ringDto.setTitle("My ring title1");
    ringDto.setDescription("My ring description1");
    ringDto.setColor("color");
    ringDto.setPosition(1);
    final var ring = ringMapper.toEntity(ringDto);

    Assertions.assertEquals(ring.getId(), ringDto.getId());
    Assertions.assertEquals(ring.getTitle(), ringDto.getTitle());
    Assertions.assertEquals(ring.getDescription(), ringDto.getDescription());
    Assertions.assertEquals(ringDto.getColor(), ring.getColor());
    Assertions.assertEquals(ringDto.getPosition(), ring.getPosition());

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

    final Ring ring = new Ring();
    ring.setId(2L);
    ring.setRadar(radar);
    ring.setTitle("My ring title");
    ring.setDescription("My ring description");
    ring.setColor("My color");
    ring.setPosition(1);

    final RingDto ringDto = new RingDto();
    ringDto.setId(2L);
    ringDto.setRadarId(radar.getId());
    ringDto.setRadarTitle(radar.getTitle());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setColor("color");
    ringDto.setPosition(1);
    ringDto.setTechnologyBlipDtoList(List.of(new TechnologyBlipDto()));

    final var technologyBlip = new TechnologyBlip();
    technologyBlip.setId(3L);
    technologyBlip.setRadar(radar);
    technologyBlip.setRing(ring);

    Mockito.when(technologyBlipMapper.toEntity(any())).thenReturn(technologyBlip);

    final var mappedRing = ringMapper.toEntity(ringDto);

    Assertions.assertEquals(mappedRing.getId(), ringDto.getId());
    Assertions.assertEquals(mappedRing.getTitle(), ringDto.getTitle());
    Assertions.assertEquals(mappedRing.getDescription(), ringDto.getDescription());
    Assertions.assertEquals(mappedRing.getColor(), ringDto.getColor());
    Assertions.assertEquals(mappedRing.getPosition(), ringDto.getPosition());
    Assertions.assertEquals(mappedRing.getId(),
        mappedRing.getTechnologyBlipList().iterator().next().getRing().getId());
    Assertions.assertEquals(mappedRing.getTitle(),
        mappedRing.getTechnologyBlipList().iterator().next().getRing().getTitle());
    Assertions.assertEquals(mappedRing.getDescription(),
        mappedRing.getTechnologyBlipList().iterator().next().getRing().getDescription());
    Assertions.assertEquals(mappedRing.getPosition(),
        mappedRing.getTechnologyBlipList().iterator().next().getRing().getPosition());
    Assertions.assertEquals(mappedRing.getRadar().getId(),
        mappedRing.getTechnologyBlipList().iterator().next().getRadar().getId());

    Assertions.assertEquals(mappedRing.getRadar().getId(), ringDto.getRadarId());
    Assertions.assertEquals(mappedRing.getRadar().getTitle(), ringDto.getRadarTitle());

    Assertions.assertNotNull(mappedRing.getTechnologyBlipList());
    Assertions.assertEquals(1, mappedRing.getTechnologyBlipList().size());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getId(), technologyBlip.getId());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getRadar().getId(),
        technologyBlip.getRadar().getId());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getRing().getId(),
        technologyBlip.getRing().getId());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getRing().getTitle(),
        technologyBlip.getRing().getTitle());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getRing().getDescription(),
        technologyBlip.getRing().getDescription());
    Assertions.assertEquals(mappedRing.getTechnologyBlipList().iterator().next().getRing().getPosition(),
        technologyBlip.getRing().getPosition());

    Mockito.verify(radarRepository).findById(radar.getId());
    Mockito.verify(technologyBlipMapper).toEntity(any());
  }
  */
}