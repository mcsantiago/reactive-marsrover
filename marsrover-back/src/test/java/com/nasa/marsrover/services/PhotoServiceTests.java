package com.nasa.marsrover.services;

import com.nasa.marsrover.services.photos.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PhotoServiceTests {
  @Test
  public void whenResourceAsStream_thenReadSuccessful() throws IOException {
    InputStream resource = new ClassPathResource("dates.txt").getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
      String dates = reader.lines().collect(Collectors.joining("\n"));
      assertEquals("02/27/17\nJune 2, 2018\nJul-13-2016\nApril 31, 2018", dates);
    }
  }
}
