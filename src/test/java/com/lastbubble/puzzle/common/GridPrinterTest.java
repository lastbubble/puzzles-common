package com.lastbubble.puzzle.common;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.function.BiPredicate;
import co.unruly.matchers.StreamMatchers;
import org.junit.Test;

public class GridPrinterTest {

  private final Grid.Builder<Integer> gridBuilder = Grid.builder(Integer.class);
  private Grid<Integer> grid;
  private CharRaster raster;

  private void whenGridIs(Grid<Integer> grid) {
    this.grid = grid;
    raster = CharRaster.builder().ofWidth(2 * grid.width() + 1).ofHeight(2 * grid.height() + 1).build();
  }

  private GridPrinter<Integer> printer() {
    return new GridPrinter<Integer>(raster::set, n -> (char) (n + '0'));
  }

  @Test public void emptyGrid() {
    whenGridIs(gridBuilder.add(Cell.at(2, 1)).build());

    printer().print(grid);

    assertThat(raster.lines(), StreamMatchers.contains(
      "┏━┯━┯━┓",
      "┃ │ │ ┃",
      "┠─┼─┼─┨",
      "┃ │ │ ┃",
      "┗━┷━┷━┛"
    ));
  }

  @Test public void gridWithValues() {
    whenGridIs(gridBuilder
      .add(Cell.at(0,0).withValue(1))
      .add(Cell.at(1,1).withValue(2))
      .add(Cell.at(2,2).withValue(3))
      .build()
    );

    printer().print(grid);

    assertThat(raster.lines(), StreamMatchers.contains(
      "┏━┯━┯━┓",
      "┃1│ │ ┃",
      "┠─┼─┼─┨",
      "┃ │2│ ┃",
      "┠─┼─┼─┨",
      "┃ │ │3┃",
      "┗━┷━┷━┛"
    ));
  }

  @Test public void gridWithRegions() {
    whenGridIs(gridBuilder.add(Cell.at(2, 2)).build());

    BiPredicate<Pos, Pos> sameSign = (a, b) -> Math.signum(a.x() - a.y()) == Math.signum(b.x() - b.y());
    printer().print(grid, sameSign);

    assertThat(raster.lines(), StreamMatchers.contains(
      "┏━┳━┯━┓",
      "┃ ┃ │ ┃",
      "┣━╋━╅─┨",
      "┃ ┃ ┃ ┃",
      "┠─╄━╋━┫",
      "┃ │ ┃ ┃",
      "┗━┷━┻━┛"
    ));
  }

  @Test public void gridWithRegionsWithBordersSuppressed() {
    whenGridIs(gridBuilder.add(Cell.at(2, 2)).build());

    BiPredicate<Pos, Pos> sameSign = (a, b) -> Math.signum(a.x() - a.y()) == Math.signum(b.x() - b.y());
    printer().suppressBorderBetweenConnectedCells().print(grid, sameSign);

    assertThat(raster.lines(), StreamMatchers.contains(
      "┏━┳━━━┓",
      "┃ ┃   ┃",
      "┣━╋━┓ ┃",
      "┃ ┃ ┃ ┃",
      "┃ ┗━╋━┫",
      "┃   ┃ ┃",
      "┗━━━┻━┛"
    ));
  }

  @Test public void gridWithPath() {
    whenGridIs(gridBuilder.add(Cell.at(2, 2)).build());

    List<Pos> path = List.of(
      Pos.at(0,0), Pos.at(1,0), Pos.at(2,0),
      Pos.at(2,1), Pos.at(2,2), Pos.at(1,2),
      Pos.at(0,2), Pos.at(0,1), Pos.at(1,1)
    );

    BiPredicate<Pos, Pos> sameSign = (a, b) -> Math.abs(path.indexOf(a) - path.indexOf(b)) == 1;
    printer().print(grid, sameSign);

    assertThat(raster.lines(), StreamMatchers.contains(
      "┏━┯━┯━┓",
      "┃ │ │ ┃",
      "┣━┿━╅─┨",
      "┃ │ ┃ ┃",
      "┠─┾━╃─┨",
      "┃ │ │ ┃",
      "┗━┷━┷━┛"
    ));
  }
}
