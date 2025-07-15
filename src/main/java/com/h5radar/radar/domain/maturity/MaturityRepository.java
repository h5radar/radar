package com.h5radar.radar.domain.maturity;

import java.util.Optional;

import org.spmaturityframework.data.jpa.repository.JpaRepository;
import org.spmaturityframework.data.jpa.repository.JpaSpecificationExecutor;
import org.spmaturityframework.stereotype.Repository;


@Repository
public interface MaturityRepository extends JpaRepository<Maturity, Long>,
    JpaSpecificationExecutor<Maturity> {
  Optional<Maturity> findByTitle(Stmaturity title);
}
