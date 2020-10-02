package com.lastbubble.puzzle.common;

import static com.lastbubble.puzzle.common.Border.Weight.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BorderTest {

  @Test public void horizontal() {
    assertEquals(" ─━",
      new StringBuilder()
        .append(Border.horizontal(NONE))
        .append(Border.horizontal(LIGHT))
        .append(Border.horizontal(HEAVY))
        .toString()
    );
  }

  @Test public void vertical() {
    assertEquals(" │┃",
      new StringBuilder()
        .append(Border.vertical(NONE))
        .append(Border.vertical(LIGHT))
        .append(Border.vertical(HEAVY))
        .toString()
    );
  }

  @Test public void useBuilder() {
    Border.Builder builder = Border.builder();
    assertEquals(" │┥┿",
      new StringBuilder()
        .append(builder.top(LIGHT).build())
        .append(builder.bottom(LIGHT).build())
        .append(builder.left(HEAVY).build())
        .append(builder.right(HEAVY).build())
        .toString()
    );
  }
}
