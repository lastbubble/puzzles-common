package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.Direction.*;
import static org.junit.Assert.*;

import java.util.function.BiPredicate;

import co.unruly.matchers.OptionalMatchers;
import org.junit.Test;

public class DirectionTest {

  private final BiPredicate<Integer, Integer> isValid = (x, y) -> (x % 2 == 0) && (y % 2 == 1);

  @Test public void validMove() {
    assertThat(UP.move(isValid, Pos.at(0,3), 2), OptionalMatchers.contains(Pos.at(0,1)));
    assertThat(DOWN.move(isValid, Pos.at(0,3), 2), OptionalMatchers.contains(Pos.at(0,5)));
    assertThat(LEFT.move(isValid, Pos.at(2,1), 2), OptionalMatchers.contains(Pos.at(0,1)));
    assertThat(RIGHT.move(isValid, Pos.at(2,1), 2), OptionalMatchers.contains(Pos.at(4,1)));
  }

  @Test public void invalidMove() {
    assertThat(UP.move(isValid, Pos.at(0,2), 2), OptionalMatchers.empty());
    assertThat(DOWN.move(isValid, Pos.at(0,2), 2), OptionalMatchers.empty());
    assertThat(LEFT.move(isValid, Pos.at(3,1), 2), OptionalMatchers.empty());
    assertThat(RIGHT.move(isValid, Pos.at(3,1), 2), OptionalMatchers.empty());
  }

  @Test public void opposite() {
    assertSame(UP.opposite(), DOWN);
    assertSame(DOWN.opposite(), UP);
    assertSame(LEFT.opposite(), RIGHT);
    assertSame(RIGHT.opposite(), LEFT);
  }
}
