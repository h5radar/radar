package com.h5radar.radar.domain.maturity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface MaturityRepository extends JpaRepository<Maturity, Long>,
    JpaSpecificationExecutor<Maturity> {
  Optional<Maturity> findByTitle(String title);
}
