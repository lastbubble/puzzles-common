package com.lastbubble.puzzle.common;

import static org.junit.Assert.assertThat;

import co.unruly.matchers.OptionalMatchers;
import org.junit.Test;

public class MoverTest {

  private final Mover move = Grid.builder(Object.class).add(Cell.at(2,2)).build().mover();

  @Test public void move_whenValid() {
    assertThat(move.up(Pos.at(1,1)), OptionalMatchers.contains(Pos.at(1,0)));
    assertThat(move.down(Pos.at(1,1)), OptionalMatchers.contains(Pos.at(1,2)));
    assertThat(move.left(Pos.at(1,1)), OptionalMatchers.contains(Pos.at(0,1)));
    assertThat(move.right(Pos.at(1,1)), OptionalMatchers.contains(Pos.at(2,1)));
  }

  @Test public void move_whenInvalid() {
    assertThat(move.up(Pos.at(1,0)), OptionalMatchers.empty());
    assertThat(move.down(Pos.at(1,2)), OptionalMatchers.empty());
    assertThat(move.left(Pos.at(0,1)), OptionalMatchers.empty());
    assertThat(move.right(Pos.at(2,1)), OptionalMatchers.empty());
  }

  @Test public void moveMultipleSteps() {
    assertThat(move.up(Pos.at(1,2), 2), OptionalMatchers.contains(Pos.at(1,0)));
    assertThat(move.down(Pos.at(1,0), 2), OptionalMatchers.contains(Pos.at(1,2)));
    assertThat(move.left(Pos.at(2,1), 2), OptionalMatchers.contains(Pos.at(0,1)));
    assertThat(move.right(Pos.at(0,1), 2), OptionalMatchers.contains(Pos.at(2,1)));
  }
}
