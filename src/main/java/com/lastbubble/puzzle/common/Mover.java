package com.lastbubble.puzzle.common;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class Mover {

  private final BiPredicate<Integer, Integer> isValid;

  Mover(BiPredicate<Integer, Integer> isValid) { this.isValid = isValid; }

  public Optional<Pos> up(Pos pos) { return up(pos, 1); }
  public Optional<Pos> down(Pos pos) { return down(pos, 1); }
  public Optional<Pos> left(Pos pos) { return left(pos, 1); }
  public Optional<Pos> right(Pos pos) { return right(pos, 1); }

  public Optional<Pos> up(Pos pos, int steps) { return move(Direction.UP, pos, steps); }
  public Optional<Pos> down(Pos pos, int steps) { return move(Direction.DOWN, pos, steps); }
  public Optional<Pos> left(Pos pos, int steps) { return move(Direction.LEFT, pos, steps); }
  public Optional<Pos> right(Pos pos, int steps) { return move(Direction.RIGHT, pos, steps); }

  public Stream<Pos> neighborsOf(Pos pos) {
    return Stream.of(
      up(pos).flatMap(this::left),
      up(pos),
      up(pos).flatMap(this::right),
      left(pos),
      right(pos),
      down(pos).flatMap(this::left),
      down(pos),
      down(pos).flatMap(this::right)
    ).flatMap(Optional::stream);
  }

  public Optional<Pos> move(Direction direction, Pos pos, int steps) { return direction.move(isValid, pos, steps); }
}
