package com.nasa.marsrover.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Utility class for date parsing */
public class DateParser {
  private static final DateTimeFormatter[] FORMATTERS =
      new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("MM/dd/uu").withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("MMMM d, uuuu").withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("MMM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
      };

  /**
   * Parses a date into a LocalDate object. Accepts the following formats:
   *
   * <p>02/27/17 June 2, 2018 Jul-13-2016
   *
   * @param date
   * @return
   */
  public static LocalDate parseDate(String date) {
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return LocalDate.parse(date, formatter);
      } catch (DateTimeParseException ignored) {
      } // Move on to the next
    }
    throw new IllegalArgumentException("Unable to parse date: " + date);
  }
}
