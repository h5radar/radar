package com.t9radar.radar.domain.segment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long>,
    JpaSpecificationExecutor<Segment> {
  Optional<Segment> findByTitle(String title);
}

