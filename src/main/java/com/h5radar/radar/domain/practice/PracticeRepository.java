package com.h5radar.radar.domain.practice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long>,
    JpaSpecificationExecutor<Practice> {
  Optional<Practice> findByTitle(String title);
}
