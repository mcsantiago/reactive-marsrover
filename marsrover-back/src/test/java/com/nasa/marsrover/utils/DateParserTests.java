package com.nasa.marsrover.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateParserTests {
  @Test
  public void parseStringSuccess() {
    assertEquals(LocalDate.of(2017, 2, 27), DateParser.parseDate("02/27/17"));
    assertEquals(LocalDate.of(2018, 6, 2), DateParser.parseDate("June 2, 2018"));
    assertEquals(LocalDate.of(2018, 6, 2), DateParser.parseDate("2 June, 2018"));
    assertEquals(LocalDate.of(2016, 7, 13), DateParser.parseDate("Jul-13-2016"));
    assertEquals(LocalDate.of(2020, 1, 2), DateParser.parseDate("2020-1-2"));
  }

  @Test
  public void throwExceptionOnBadDate() {
    assertThrows(IllegalArgumentException.class, () -> DateParser.parseDate("April 31, 2018"));
  }
}
