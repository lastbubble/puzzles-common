package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.RandomNumbers.*;
import static org.junit.Assert.*;

import java.util.stream.IntStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PosTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test public void create() {
    int x = naturalNumber(), y = naturalNumber();
    Pos pos = Pos.at(x, y);

    assertEquals("x is first argument", x, pos.x());
    assertEquals("y is second argument", y, pos.y());
  }

  @Test public void create_withInvalidX() {
    int x = negativeNumber(), y = naturalNumber();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("x cannot be negative: " + x);

    Pos.at(x, y);
  }

  @Test public void create_withInvalidY() {
    int x = naturalNumber(), y = negativeNumber();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("y cannot be negative: " + y);

    Pos.at(x, y);
  }

  @Test public void cachedValues() {
    IntStream.range(0, 16).forEach(x ->
      IntStream.range(0, 16).forEach(y -> {
        Pos pos = Pos.at(x, y);

        assertEquals(x, pos.x());
        assertEquals(y, pos.y());
        assertSame("common values are cached", pos, Pos.at(x, y));
      })
    );
  }

  @Test public void uncachedValues() {
    int greaterThan16 = naturalNumberThat(n -> n > 16);
    int lessThan16 = naturalNumberThat(n -> n < 16);

    assertNotSame(Pos.at(lessThan16, greaterThan16), Pos.at(lessThan16, greaterThan16));
    assertNotSame(Pos.at(greaterThan16, lessThan16), Pos.at(greaterThan16, lessThan16));
  }

  @Test public void equals_implemented() {
    // use large numbers to avoid caching
    int x = naturalNumber() + 100, y = naturalNumber() + 100;
    Pos pos = Pos.at(x, y);

    assertEquals("equals itself", pos, pos);
    assertNotEquals("not equal to other objects", pos, new Object());
    assertNotEquals("not equal if x differs", pos, Pos.at(naturalNumberThat(n -> n != x), y));
    assertNotEquals("not equal if y differs", pos, Pos.at(x, naturalNumberThat(n -> n != y)));
    assertEquals("equal if x and y are the same", pos, Pos.at(x, y));
  }

  @Test public void hashCode_implemented() {
    // use large numbers to avoid caching
    int x = naturalNumber() + 100, y = naturalNumber() + 100;
    Pos pos = Pos.at(x, y);

    assertEquals(pos.hashCode(), Pos.at(x, y).hashCode());
  }

  @Test public void toString_implemented() {
    int x = naturalNumber(), y = naturalNumber();

    assertEquals("("+x+","+y+")", Pos.at(x, y).toString());
  }
}
