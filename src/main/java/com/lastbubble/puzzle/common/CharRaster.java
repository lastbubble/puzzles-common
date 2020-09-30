package com.lastbubble.puzzle.common;

import java.util.Arrays;
import java.util.stream.Stream;

public class CharRaster {

  private final char[][] lines;

  private CharRaster(Builder builder) {
    int width = builder.width;
    int height = builder.height;

    assertPositive(width, "width");
    assertPositive(height, "height");

    lines = new char[height][width];

    for (int y = 0; y < height; y++) { Arrays.fill(lines[y], ' '); }
  }

  private static void assertPositive(int n, String name) {
    if (n <= 0) {
      throw new IllegalArgumentException(String.format("invalid %s: %s", name, n));
    }
  }

  public void set(Pos pos, char value) { lines[pos.y()][pos.x()] = value; }

  public Stream<String> lines() { return Arrays.stream(lines).map(chars -> new String(chars)); }

  public static Builder builder() { return new Builder(); }

  public static class Builder {

    private int width;
    private int height;

    public Builder ofWidth(int n) { width = n; return this; }

    public Builder ofHeight(int n) { height = n; return this; }

    public CharRaster build() { return new CharRaster(this); }
  }
}
