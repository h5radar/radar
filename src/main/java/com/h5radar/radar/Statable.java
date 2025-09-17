package com.h5radar.radar;

import org.springframework.data.domain.Sort;

public record Statable(Sort sort) {
  public static Statable unsorted() {
    return new Statable(Sort.unsorted());
  }
}
