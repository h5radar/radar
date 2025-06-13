package com.h5radar.radar.domain.radar_user;

import com.h5radar.radar.domain.JpaConstants;
import com.h5radar.radar.domain.ring.Ring;
import com.h5radar.radar.domain.technology.Technology;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.radar.domain.AbstractAuditable;

import java.util.List;

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
  private String sub;

  @NotBlank
  @Size(min = 1, max = 255)
  @RadarUserTrimUsernameConstraint
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radar_user", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<Technology> technologyList;

}
