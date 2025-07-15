package com.h5radar.radar.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>,
    JpaSpecificationExecutor<Domain> {
  Optional<Domain> findByTitle(String title);
}

