package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.RandomNumbers.*;
import static org.junit.Assert.*;

import co.unruly.matchers.OptionalMatchers;
import co.unruly.matchers.StreamMatchers;
import org.junit.Test;

public class GridTest {

  private final Grid.Builder<String> builder = Grid.builder(String.class);

  private Grid<String> grid;

  @Test public void emptyGrid() {
    grid = builder.build();

    assertEquals(0, grid.width());
    assertEquals(0, grid.height());
  }

  @Test public void withWidthAndHeight() {
    int x = positiveNumber();
    int y = positiveNumberThat(n -> n != x);

    grid = builder.add(Cell.at(x, y)).build();

    assertEquals(x + 1, grid.width());
    assertEquals(y + 1, grid.height());
  }

  @Test public void squareGrid() {
    int size = positiveNumber();

    grid = builder.squareWithSize(size).build();

    assertEquals(size, grid.width());
    assertEquals(size, grid.height());
  }

  @Test public void withValues() {
    grid = builder
      .add(Cell.at(0,1).withValue("abc"))
      .add(Cell.at(1,0).withValue("def"))
      .build();

    assertThat(grid.valueAt(0,0), OptionalMatchers.empty());
    assertThat(grid.valueAt(0,1), OptionalMatchers.contains("abc"));
    assertThat(grid.valueAt(1,0), OptionalMatchers.contains("def"));
    assertThat(grid.valueAt(1,1), OptionalMatchers.empty());
  }

  @Test public void valueAt_usingPos() {
    grid = builder
      .add(Cell.at(0,1).withValue("abc"))
      .add(Cell.at(1,0).withValue("def"))
      .build();

    assertEquals(grid.valueAt(0,0), grid.valueAt(Pos.at(0,0)));
    assertEquals(grid.valueAt(1,0), grid.valueAt(Pos.at(1,0)));
    assertEquals(grid.valueAt(0,1), grid.valueAt(Pos.at(0,1)));
    assertEquals(grid.valueAt(1,1), grid.valueAt(Pos.at(1,1)));
  }

  @Test public void positions_whenSquare() {
    grid = builder.add(Cell.at(2,2)).build();

    assertThat(grid.positions(), StreamMatchers.contains(
        Pos.at(0,0), Pos.at(1,0), Pos.at(2,0),
        Pos.at(0,1), Pos.at(1,1), Pos.at(2,1),
        Pos.at(0,2), Pos.at(1,2), Pos.at(2,2)
      )
    );
  }

  @Test public void positions_whenRectangular() {
    grid = builder.add(Cell.at(2,1)).build();

    assertThat(grid.positions(), StreamMatchers.contains(
        Pos.at(0,0), Pos.at(1,0), Pos.at(2,0),
        Pos.at(0,1), Pos.at(1,1), Pos.at(2,1)
      )
    );
  }

  @Test public void cellsMatching() {
    grid = builder
      .add(Cell.at(0,0).withValue("e"))
      .add(Cell.at(2,0).withValue("D"))
      .add(Cell.at(1,1).withValue("C"))
      .add(Cell.at(0,2).withValue("b"))
      .add(Cell.at(2,2).withValue("a"))
      .build();

    assertThat(
      grid.cellsMatching(c -> c.value().map(s -> s.equals(s.toUpperCase())).orElse(false)),
      StreamMatchers.contains(Cell.at(2,0).withValue("D"), Cell.at(1,1).withValue("C"))
    );
  }

  @Test public void copy() {
    int size = positiveNumber() + 2;
    grid = builder
      .squareWithSize(size)
      .add(Cell.at(0,1).withValue("abc"))
      .add(Cell.at(1,0).withValue("def"))
      .build();

    Grid<String> otherGrid = grid.copy().add(Cell.at(1,0).withValue("ghi")).build();

    assertEquals(size, otherGrid.width());
    assertEquals(size, otherGrid.height());
    assertThat(
      otherGrid.cellsMatching(c -> c.value().isPresent()),
      StreamMatchers.contains(Cell.at(1,0).withValue("ghi"), Cell.at(0,1).withValue("abc"))
    );
  }
}
