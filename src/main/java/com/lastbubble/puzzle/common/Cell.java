package com.lastbubble.puzzle.common;

import java.util.Objects;
import java.util.Optional;

public class Cell<V> {

  public static <V> Cell<V> at(int x, int y) { return new Cell<V>(Pos.at(x, y)); }

  public static <V> Cell<V> at(Pos pos) { return new Cell<V>(pos); }

  private final Pos pos;
  private final V value;

  private Cell(Pos pos) { this(pos, null); }

  private Cell(Pos pos, V value) {
    this.pos = pos;
    this.value = value;
  }

  public Pos pos() { return pos; }

  public Optional<V> value() { return Optional.ofNullable(value); }

  public <D> Cell<D> withValue(D value) { return new Cell<D>(pos, value); }

  @Override public int hashCode() { return Objects.hash(pos, value); }

  @Override public boolean equals(Object obj) {
    if (obj == this) { return true; }

    if (obj instanceof Cell<?>) {
      Cell<?> that = (Cell<?>) obj;

      return (
        Objects.equals(this.pos, that.pos) &&
        Objects.equals(this.value, that.value)
      );
    }

    return false;
  }

  @Override public String toString() {
    return String.format("%s%s", pos, (value != null) ? ("=" + value) : "");
  }

}
