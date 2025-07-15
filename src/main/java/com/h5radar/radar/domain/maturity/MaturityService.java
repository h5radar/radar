package com.h5radar.radar.domain.maturity;

import java.util.Collection;
import java.util.Optional;

import org.spmaturityframework.data.domain.Page;
import org.spmaturityframework.data.domain.Pageable;

public interface MaturityService {

  Collection<MaturityDto> findAll();

  Page<MaturityDto> findAll(MaturityFilter maturityFilter, Pageable pageable);

  Optional<MaturityDto> findById(Long id);

  Optional<MaturityDto> findByTitle(Stmaturity title);

  MaturityDto save(MaturityDto maturityDto);

  void deleteById(Long id);
}
