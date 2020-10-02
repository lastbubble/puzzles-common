package com.lastbubble.puzzle.common;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RandomNumbers {

  private static final PrimitiveIterator.OfInt naturalNumbers = new Random().ints(0, 100).iterator();
  private static final PrimitiveIterator.OfInt positiveNumbers = new Random().ints(1, 100).iterator();

  public static int naturalNumber() { return naturalNumbers.nextInt(); }

  public static int positiveNumber() { return positiveNumbers.nextInt(); }

  public static int negativeNumber() { return -1 * positiveNumber(); }

  public static int naturalNumberThat(Predicate<Integer> matches) {
    return numberThat(RandomNumbers::naturalNumber, matches);
  }

  public static int positiveNumberThat(Predicate<Integer> matches) {
    return numberThat(RandomNumbers::positiveNumber, matches);
  }

  public static int numberThat(Supplier<Integer> supplier, Predicate<Integer> matches) {
    int n;
    do { n = supplier.get(); } while (!matches.test(n));
    return n;
  }
}
