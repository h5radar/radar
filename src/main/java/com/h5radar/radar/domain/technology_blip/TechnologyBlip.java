package com.h5radar.radar.domain.technology_blip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.radar.domain.AbstractAuditable;
import com.h5radar.radar.domain.radar.Radar;
import com.h5radar.radar.domain.ring.Ring;
import com.h5radar.radar.domain.segment.Segment;
import com.h5radar.radar.domain.technology.Technology;


@Entity
@Table(name = "technology_blips")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyBlip extends AbstractAuditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "technology_id", nullable = false)
  private Technology technology;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "segment_id", nullable = false)
  private Segment segment;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "ring_id", nullable = false)
  private Ring ring;
}
