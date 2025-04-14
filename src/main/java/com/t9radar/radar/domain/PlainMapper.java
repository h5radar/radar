package com.t9radar.radar.domain;

public interface PlainMapper<E, D> {

  D toDto(final E entity);

  E toEntity(final D dto);

}
