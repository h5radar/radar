package com.h5radar.radar.domain;

import static org.mockito.ArgumentMatchers.any;

import com.h5radar.radar.AbstractMapperTests;

class DomainMapperTests extends AbstractMapperTests {
  /* TODO: uncomment

  @MockitoBean
  private RadarRepository radarRepository;

  @MockitoBean
  private TechnologyBlipMapper technologyBlipMapper;

  @Autowired
  private DomainMapper domainMapper;

  @Test
  public void testToDtoWithNull() {
    final var domainDto = domainMapper.toDto(null);

    Assertions.assertNull(domainDto);
  }

  @Test
  public void testToDtoAllFields() {
    final var domain = new Domain();
    domain.setId(1L);
    domain.setTitle("My domain title");
    domain.setDescription("My domain description");
    domain.setPosition(1);

    final var domainDto = domainMapper.toDto(domain);

    Assertions.assertEquals(domain.getId(), domainDto.getId());
    Assertions.assertEquals(domain.getTitle(), domainDto.getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDto.getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDto.getPosition());
  }

  @Test
  public void testToDtoAllLists() {
    final RadarType radarType = new RadarType();
    radarType.setId(10L);
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarType.setTitle("My radarType title");
    radarType.setDescription("My radarType description");

    // Create radar
    final Radar radar = new Radar();
    radar.setId(1L);
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setRingList(List.of(new Ring()));
    radar.setTechnologyBlipList(List.of(new TechnologyBlip()));
    radar.setDomainList(List.of(new Domain()));

    // Create ring
    final RingDto ringDto = new RingDto();
    ringDto.setId(2L);
    ringDto.setRadarId(radar.getId());
    ringDto.setRadarTitle(radar.getTitle());
    ringDto.setTitle("My ring title");
    ringDto.setDescription("My ring description");
    ringDto.setColor("My ring color");
    ringDto.setPosition(1);
    ringDto.setTechnologyBlipDtoList(List.of(new TechnologyBlipDto()));

    // Create domain
    final Domain domain = new Domain();
    domain.setId(3L);
    domain.setRadar(radar);
    domain.setTitle("My domain title");
    domain.setDescription("My domain description");
    domain.setPosition(2);
    domain.setTechnologyBlipList(List.of(new TechnologyBlip()));

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
    technologyBlipDto.setDomainId(domain.getId());
    technologyBlipDto.setDomainTitle(domain.getTitle());
    technologyBlipDto.setDomainPosition(domain.getPosition());
    technologyBlipDto.setTechnologyId(technologyDto.getId());
    technologyBlipDto.setTechnologyTitle(technologyDto.getTitle());
    technologyBlipDto.setTechnologyWebsite(technologyDto.getWebsite());
    technologyBlipDto.setTechnologyMoved(technologyDto.getMoved());
    technologyBlipDto.setRingId(ringDto.getId());
    technologyBlipDto.setRingTitle(ringDto.getTitle());
    technologyBlipDto.setRingPosition(ringDto.getPosition());

    Mockito.when(technologyBlipMapper.toDto(any())).thenReturn(technologyBlipDto);
    final var domainDto = domainMapper.toDto(domain);
    Mockito.verify(technologyBlipMapper).toDto(any());

    Assertions.assertEquals(domain.getId(), domainDto.getId());
    Assertions.assertEquals(domain.getTitle(), domainDto.getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDto.getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDto.getPosition());

    Assertions.assertEquals(domain.getRadar().getId(), domainDto.getRadarId());
    Assertions.assertEquals(domain.getRadar().getTitle(), domainDto.getRadarTitle());

    Assertions.assertNotNull(domainDto.getTechnologyBlipDtoList());
    Assertions.assertEquals(1, domainDto.getTechnologyBlipDtoList().size());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getId(), technologyBlipDto.getId());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getRadarTitle(),
        technologyBlipDto.getRadarTitle());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getTechnologyId(),
        technologyBlipDto.getTechnologyId());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getTechnologyTitle(),
        technologyBlipDto.getTechnologyTitle());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getTechnologyWebsite(),
        technologyBlipDto.getTechnologyWebsite());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getTechnologyMoved(),
        technologyBlipDto.getTechnologyMoved());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().isTechnologyActive(),
        technologyBlipDto.isTechnologyActive());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getDomainId(),
        technologyBlipDto.getDomainId());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getDomainTitle(),
        technologyBlipDto.getDomainTitle());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getDomainPosition(),
        technologyBlipDto.getDomainPosition());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getRingId(),
        technologyBlipDto.getRingId());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getRingTitle(),
        technologyBlipDto.getRingTitle());
    Assertions.assertEquals(domainDto.getTechnologyBlipDtoList().iterator().next().getRingPosition(),
        technologyBlipDto.getRingPosition());

  }

  @Test
  public void testToEntityWithNull() {
    final var domain = domainMapper.toEntity(null);

    Assertions.assertNull(domain);
  }

  @Test
  public void testToEntityAllFields() {
    final Radar radar = new Radar();
    radar.setId(0L);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(true);

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    final var domainDto = new DomainDto();
    domainDto.setId(1L);
    domainDto.setRadarId(radar.getId());
    domainDto.setRadarTitle(radar.getTitle());
    domainDto.setTitle("My domain title");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(1);

    final var domain = domainMapper.toEntity(domainDto);
    Mockito.verify(radarRepository).findById(domain.getRadar().getId());

    Assertions.assertEquals(domain.getId(), domainDto.getId());
    Assertions.assertEquals(domain.getTitle(), domainDto.getTitle());
    Assertions.assertEquals(domain.getDescription(), domainDto.getDescription());
    Assertions.assertEquals(domain.getPosition(), domainDto.getPosition());

  }

  @Test
  public void testToEntityAllLists() {
    final RadarType radarType = new RadarType();
    radarType.setId(1L);
    radarType.setCode(RadarType.TECHNOLOGY_RADAR);
    radarType.setTitle("My radarType title");
    radarType.setDescription("My radarType description");

    final Radar radar = new Radar();
    radar.setId(2L);
    radar.setRadarType(radarType);
    radar.setTitle("My radar title");
    radar.setDescription("My radar description");
    radar.setPrimary(true);
    radar.setActive(true);
    radar.setDomainList(List.of(new Domain()));
    radar.setTechnologyBlipList(List.of(new TechnologyBlip()));

    Mockito.when(radarRepository.findById(any())).thenReturn(Optional.of(radar));

    final Domain domain = new Domain();
    domain.setId(3L);
    domain.setRadar(radar);
    domain.setTitle("My domain title");
    domain.setDescription("My domain description");
    domain.setPosition(1);

    final var domainDto = new DomainDto();
    domainDto.setId(3L);
    domainDto.setRadarId(radar.getId());
    domainDto.setRadarTitle(radar.getTitle());
    domainDto.setTitle("My domain title");
    domainDto.setDescription("My domain description");
    domainDto.setPosition(1);
    domainDto.setTechnologyBlipDtoList(List.of(new TechnologyBlipDto()));

    final var technologyBlip = new TechnologyBlip();
    technologyBlip.setId(4L);
    technologyBlip.setRadar(radar);
    technologyBlip.setDomain(domain);

    Mockito.when(technologyBlipMapper.toEntity(any())).thenReturn(technologyBlip);

    final var MappedDomain = domainMapper.toEntity(domainDto);

    Assertions.assertEquals(MappedDomain.getId(), domainDto.getId());
    Assertions.assertEquals(MappedDomain.getTitle(), domainDto.getTitle());
    Assertions.assertEquals(MappedDomain.getDescription(), domainDto.getDescription());
    Assertions.assertEquals(MappedDomain.getPosition(), domainDto.getPosition());

    Assertions.assertEquals(MappedDomain.getRadar().getId(), domainDto.getRadarId());
    Assertions.assertEquals(MappedDomain.getRadar().getTitle(), domainDto.getRadarTitle());
    Assertions.assertEquals(MappedDomain.getId(),
        MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getId());
    Assertions.assertEquals(MappedDomain.getTitle(),
        MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getTitle());
    Assertions.assertEquals(MappedDomain.getDescription(),
        MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getDescription());
    Assertions.assertEquals(MappedDomain.getPosition(),
        MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getPosition());
    Assertions.assertEquals(MappedDomain.getRadar().getId(),
        MappedDomain.getTechnologyBlipList().iterator().next().getRadar().getId());

    Assertions.assertNotNull(MappedDomain.getTechnologyBlipList());
    Assertions.assertEquals(1, MappedDomain.getTechnologyBlipList().size());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getId(), technologyBlip.getId());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getRadar().getId(),
        technologyBlip.getRadar().getId());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getRadar().getTitle(),
        technologyBlip.getRadar().getTitle());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getId(),
        technologyBlip.getDomain().getId());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getTitle(),
        technologyBlip.getDomain().getTitle());
    Assertions.assertEquals(MappedDomain.getTechnologyBlipList().iterator().next().getDomain().getPosition(),
        technologyBlip.getDomain().getPosition());

    Mockito.verify(radarRepository).findById(MappedDomain.getRadar().getId());
    Mockito.verify(technologyBlipMapper).toEntity(any());
  }
  */
}
