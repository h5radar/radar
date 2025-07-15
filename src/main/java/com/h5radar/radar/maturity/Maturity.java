package com.h5radar.radar.domain.maturity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.radar.domain.AbstractAuditable;
import com.h5radar.radar.domain.radar_user.RadarUser;


@Entity
@Table(name = "maturities")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Maturity extends AbstractAuditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "radar_user_id", nullable = false)
  private RadarUser radarUser;

  @NotBlank
  @Size(min = 1, max = 64)
  @MaturityTrimTitleConstraint
  @MaturityUppercaseTitleConstraint
  @Column(name = "title", unique = true, nullable = false)
  private String title;

  @NotBlank
  @Size(min = 1, max = 512)
  @Column(name = "description", nullable = false)
  private String description;

  @Min(0)
  @Max(512)
  @Column(name = "position", nullable = false)
  private int position;

  @NotBlank
  @Size(min = 1, max = 8)
  @Column(name = "color", nullable = false)
  private String color;

}
