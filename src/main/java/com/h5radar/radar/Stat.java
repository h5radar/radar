package com.h5radar.radar;

import java.util.List;

public record Stat<T>(
    long total,
    SortMeta sort,
    List<T> content
) {
  public record SortMeta(boolean sorted, boolean empty, boolean unsorted) {
    public static SortMeta sorted() {
      return new SortMeta(true, false, false);
    }

    public static SortMeta unsorted() {
      return new SortMeta(false, true, true);
    }
  }
}
