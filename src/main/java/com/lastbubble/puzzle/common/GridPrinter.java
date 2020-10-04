package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.Border.Weight.*;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

public class GridPrinter<V> {

  private final BiConsumer<Pos, Character> consumer;
  private final Function<V, Character> toChar;

  public GridPrinter(BiConsumer<Pos, Character> consumer, Function<V, Character> toChar) {
    this.consumer = consumer;
    this.toChar = toChar;
  }

  private Border.Weight connectedWeight = LIGHT;
  public GridPrinter<V> suppressBorderBetweenConnectedCells() { connectedWeight = NONE; return this; }

  public void print(Grid<V> grid) { print(grid, (a, b) -> true); }

  public void print(Grid<V> grid, BiPredicate<Pos, Pos> areConnected) {
    BiFunction<Pos, Optional<Pos>, Border.Weight> weightFor =
      (a, b) -> b.map(x -> areConnected.test(a, x)).orElse(false) ? connectedWeight : HEAVY;

    Mover move = grid.mover();

    grid.positions().forEach(pos -> {
      Optional<Pos> above = move.up(pos);
      Optional<Pos> left = move.left(pos);

      int consumerX = pos.x() * 2 + 1, consumerY = pos.y() * 2 + 1;

      // upper left
      consumer.accept(Pos.at(consumerX - 1, consumerY - 1),
        Border.builder()
          .top(above.map(x -> weightFor.apply(x, move.left(x))).orElse(NONE))
          .left(left.map(x -> weightFor.apply(x, move.up(x))).orElse(NONE))
          .right(weightFor.apply(pos, above))
          .bottom(weightFor.apply(pos, left))
          .build()
      );
      // above
      consumer.accept(Pos.at(consumerX, consumerY - 1), Border.horizontal(weightFor.apply(pos, above)));
      // left
      consumer.accept(Pos.at(consumerX - 1, consumerY), Border.vertical(weightFor.apply(pos, left)));
      // value
      grid.valueAt(pos).map(toChar).ifPresent(c -> consumer.accept(Pos.at(consumerX, consumerY), c));
    });
    // right edge
    IntStream.range(0, grid.height()).forEach(y -> {
      int consumerX = 2 * grid.width(), consumerY = 2 * y;
      Pos pos = Pos.at(grid.width() - 1, y);
      consumer.accept(Pos.at(consumerX, consumerY),
        Border.builder()
          .top(move.up(pos).map(p -> HEAVY).orElse(NONE))
          .left(weightFor.apply(pos, move.up(pos)))
          .bottom(HEAVY)
          .build()
      );
      consumer.accept(Pos.at(consumerX, consumerY + 1), Border.vertical(HEAVY));
    });
    // bottom edge
    IntStream.range(0, grid.width()).forEach(x -> {
      int consumerX = 2 * x, consumerY = 2 * grid.height();
      Pos pos = Pos.at(x, grid.height() - 1);
      consumer.accept(Pos.at(consumerX, consumerY),
        Border.builder()
          .top(weightFor.apply(pos, move.left(pos)))
          .left(move.left(pos).map(p -> HEAVY).orElse(NONE))
          .right(HEAVY)
          .build()
      );
      consumer.accept(Pos.at(consumerX + 1, consumerY), Border.horizontal(HEAVY));
    });
    // bottom right corner
    consumer.accept(Pos.at(2 * grid.width(), 2 * grid.height()),
      Border.builder().top(HEAVY).left(HEAVY).build()
    );
  }
}
