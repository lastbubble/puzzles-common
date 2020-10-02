package com.lastbubble.puzzle.common;

import java.util.Optional;
import java.util.function.BiPredicate;

public enum Direction {

  UP(0,-1),
  DOWN(0,1),
  LEFT(-1,0),
  RIGHT(1,0);

  private final int deltaX;
  private final int deltaY;

  private Direction(int deltaX, int deltaY) {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  public Optional<Pos> move(BiPredicate<Integer, Integer> isValid, Pos pos, int steps) {
    int newX = pos.x() + deltaX * steps;
    int newY = pos.y() + deltaY * steps;
    return isValid.test(newX, newY) ? Optional.of(Pos.at(newX, newY)) : Optional.empty();
  }
}
