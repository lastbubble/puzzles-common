package com.lastbubble.puzzle.common;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class Pos {

  public static final BiPredicate<Integer, Integer> VALID = (x, y) -> (x >= 0) && (y >= 0);

  private static Pos[] cache = new Pos[16 * 16];
  static {
    IntStream.range(0, cache.length).forEach(n -> cache[n] = new Pos(n >> 4, n & 0xf));
  }

  public static Pos at(int x, int y) {
    assertNonNegative(x, "x");
    assertNonNegative(y, "y");
    return (x < 16 && y < 16) ? cache[(x << 4) | y] : new Pos(x, y);
  }

  private static void assertNonNegative(int value, String name) {
    if (value < 0) {
      throw new IllegalArgumentException(String.format("%s cannot be negative: %s", name, value));
    }
  }

  private final int x;
  private final int y;

  private Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int x() { return x; }
  public int y() { return y; }

  @Override public int hashCode() { return Objects.hash(x, y); }

  @Override public boolean equals(Object obj) {
    if (obj == this) { return true; }

    if (obj instanceof Pos) {
      Pos that = (Pos) obj;
      return (this.x() == that.x() && this.y() == that.y());
    }

    return false;
  }

  @Override public String toString() { return String.format("(%s,%s)", x, y); }
}
