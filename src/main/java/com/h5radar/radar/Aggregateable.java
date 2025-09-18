package com.h5radar.radar;

import org.springframework.data.domain.Sort;

public record Aggregateable(Sort sort) {
  public static Aggregateable unsorted() {
    return new Aggregateable(Sort.unsorted());
  }
}
