package com.h5radar.radar.domain.radar_user;

import com.h5radar.radar.domain.compliance.Compliance;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.radar.domain.AbstractAuditable;
import com.h5radar.radar.domain.JpaConstants;
import com.h5radar.radar.domain.license.License;
import com.h5radar.radar.domain.practice.Practice;
import com.h5radar.radar.domain.product.Product;
import com.h5radar.radar.domain.technology.Technology;

@Entity
@Table(name = "radar_users")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RadarUser extends AbstractAuditable {
  public RadarUser(Long id) {
    this.id = id;
  }

  public RadarUser(Long id, String sub, String username) {
    this.id = id;
    this.sub = sub;
    this.username = username;
  }

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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radarUser", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<Compliance> complianceList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radarUser", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<License> licenseList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radarUser", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<Practice> practiceList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radarUser", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<Technology> technologyList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "radarUser", cascade = CascadeType.ALL)
  @BatchSize(size = JpaConstants.BATCH_SIZE_FOR_COLLECTIONS)
  private List<Product> productList;
}
