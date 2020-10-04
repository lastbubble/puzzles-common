package com.lastbubble.puzzle.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid<V> {

  private final Class<V> valueClass;
  private final V[][] values;
  private final int width;
  private final int height;

  private Grid(Class<V> valueClass, V[][] values) {
    this.valueClass = valueClass;
    this.values = values;
    width = values.length;
    height = (values.length > 0) ? values[0].length : 0;
  }

  public int width() { return width; }

  public int height() { return height; }

  public Stream<Pos> positions() {
    return IntStream.range(0, width * height).mapToObj(n -> {
      int row = n / width;
      return Pos.at(n - (width * row), row);
    });
  }

  public Stream<Cell<V>> cellsMatching(Predicate<Cell<V>> matcher) {
    return positions().map(p -> Cell.at(p).withValue(values[p.x()][p.y()])).filter(matcher);
  }

  public Mover mover() { return new Mover(Pos.VALID.and((x, y) -> (x < width) && (y < height))); }

  public Optional<V> valueAt(Pos pos) { return valueAt(pos.x(), pos.y()); }

  public Optional<V> valueAt(int x, int y) { return Optional.ofNullable(values[x][y]); }

  public Builder<V> copy() {
    Grid.Builder<V> builder = builder(valueClass).add(Cell.at(width - 1, height - 1));
    cellsMatching(c -> c.value().isPresent()).forEach(c -> builder.add(c));
    builder.addedCells = false;
    return builder;
  }

  public static <V> Builder<V> builder(Class<V> valueClass) { return new Builder<V>(valueClass); }

  public static class Builder<V> {

    private final Class<V> valueClass;
    private final List<Cell<V>> cells = new ArrayList<>();

    private int xMax = -1;
    private int yMax = -1;
    private boolean addedCells;

    private Builder(Class<V> valueClass) { this.valueClass = valueClass; }

    public Builder<V> add(Cell<V> cell) {
      xMax = Math.max(xMax, cell.pos().x());
      yMax = Math.max(yMax, cell.pos().y());

      cells.add(cell);
      addedCells = true;

      return this;
    }

    public Builder<V> squareWithSize(int size) { return add(Cell.at(size - 1, size - 1)); }

    public boolean addedCells() { return addedCells; }

    @SuppressWarnings("unchecked")
    public Grid<V> build() {
      V[][] values = (V[][]) Array.newInstance(valueClass, xMax + 1, yMax + 1);

      cells.stream().forEach(c -> values[c.pos().x()][c.pos().y()] = c.value().orElse(null));

      return new Grid<V>(valueClass, values);
    }
  }
}
