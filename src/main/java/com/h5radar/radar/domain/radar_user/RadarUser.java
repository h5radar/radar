package com.h5radar.radar.domain.radar_user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.radar.domain.AbstractAuditable;

@Entity
@Table(name = "radar_users")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RadarUser extends AbstractAuditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 255)
  @RadarUserTrimSubConstraint
  @Column(name = "sub", unique = true, nullable = false)
  private String title;

  @NotBlank
  @Size(min = 1, max = 255)
  @RadarUserTrimUsernameConstraint
  @Column(name = "username", unique = true, nullable = false)
  private String title;
}
