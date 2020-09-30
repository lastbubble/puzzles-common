package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.RandomNumbers.*;
import static org.junit.Assert.assertThat;

import co.unruly.matchers.StreamMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharRasterTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  private CharRaster.Builder builder = CharRaster.builder();

  private CharRaster raster;

  @Test public void whenSizeNotSpecified() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid width: 0");

    builder.build();
  }

  @Test public void whenHeightNotSpecified() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid height: 0");

    builder.ofWidth(positiveNumber()).build();
  }

  @Test public void whenInvalidHeightSpecified() {
    int height = negativeNumber();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid height: " + height);

    builder.ofWidth(positiveNumber()).ofHeight(height).build();
  }

  @Test public void whenWidthNotSpecified() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid width: 0");

    builder.ofHeight(positiveNumber()).build();
  }

  @Test public void whenInvalidWidthSpecified() {
    int width = negativeNumber();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid width: " + width);

    builder.ofWidth(width).ofHeight(positiveNumber()).build();
  }

  @Test public void whenEmpty() {
    raster = builder.ofWidth(3).ofHeight(2).build();

    assertLinesAre("   ", "   ");
  }

  @Test public void withValues() {
    raster = builder.ofWidth(3).ofHeight(2).build();
    raster.set(Pos.at(2,0), 'a');
    raster.set(Pos.at(0,1), 'b');

    assertLinesAre("  a", "b  ");
  }

  @Test public void overwriteValues() {
    raster = builder.ofWidth(3).ofHeight(2).build();
    raster.set(Pos.at(2,0), 'a');
    raster.set(Pos.at(0,1), 'b');
    raster.set(Pos.at(2,0), 'c');
    raster.set(Pos.at(0,1), 'd');

    assertLinesAre("  c", "d  ");
  }

  private void assertLinesAre(String... lines) {
    assertThat(raster.lines(), StreamMatchers.contains(lines));
  }

  @Test public void setValueForInvalidPosition() {
    int width = positiveNumber(), height = positiveNumber();
    raster = builder.ofWidth(width).ofHeight(height).build();

    thrown.expect(ArrayIndexOutOfBoundsException.class);

    raster.set(Pos.at(width, height), 'a');
  }
}
