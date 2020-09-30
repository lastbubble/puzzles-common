package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.RandomNumbers.*;
import static org.junit.Assert.*;

import co.unruly.matchers.OptionalMatchers;
import org.junit.Test;

public class CellTest {

  private final Pos pos = Pos.at(naturalNumber(), naturalNumber());
  private final String value = "value";

  private Cell<String> cell;

  @Test public void assignPositionWithoutValue() {
    cell = Cell.at(pos);

    assertSame(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.empty());
  }

  @Test public void assignPositionWithXAndY() {
    cell = Cell.at(pos.x(), pos.y());

    assertEquals(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.empty());
  }

  @Test public void assignNullValue() {
    Cell<String> cellWithoutValue = Cell.at(pos);
    cell = cellWithoutValue.withValue(null);

    assertNotSame(cell, cellWithoutValue);
    assertSame(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.empty());
  }

  @Test public void assignValue() {
    Cell<String> cellWithoutValue = Cell.at(pos);
    cell = cellWithoutValue.withValue(value);

    assertNotSame(cell, cellWithoutValue);
    assertSame(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.contains(value));
  }

  @Test public void assignValueOfDifferentType() {
    Cell<Integer> cellWithInt = Cell.at(pos).withValue(1);
    cell = cellWithInt.withValue(value);

    assertNotSame(cell, cellWithInt);
    assertSame(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.contains(value));
  }

  @Test public void reassignValue() {
    Cell<String> cellWithValue1 = Cell.at(pos).withValue(value);
    String otherValue = "otherValue";
    cell = cellWithValue1.withValue(otherValue);

    assertNotSame(cell, cellWithValue1);
    assertSame(pos, cell.pos());
    assertThat(cell.value(), OptionalMatchers.contains(otherValue));
  }

  @Test public void hashCodeImplemented() {
    cell = Cell.at(pos).withValue(value);

    assertEquals(cell.hashCode(), Cell.at(pos).withValue(value).hashCode());
  }

  @Test public void equalsImplemented() {
    cell = Cell.at(pos);

    assertEquals(cell, cell);
    assertNotEquals(cell, new Object());
    assertNotEquals(cell, Cell.at(pos.x(), naturalNumberThat(n -> n != pos.y())));
    assertNotEquals(cell, Cell.at(naturalNumberThat(n -> n != pos.x()), pos.y()));
    assertNotEquals(cell, Cell.at(pos).withValue(value));
    assertEquals(cell, Cell.at(pos));
    assertEquals(cell.withValue(value), Cell.at(pos).withValue(value));
    assertNotEquals(cell.withValue(value), Cell.at(pos).withValue(23));
  }

  @Test public void toStringImplemented() {
    cell = Cell.at(pos);

    assertEquals(cell.toString(), String.format("(%d,%d)", pos.x(), pos.y()));
    assertEquals(cell.withValue(value).toString(), String.format("(%d,%d)=%s", pos.x(), pos.y(), value));
  }
}
