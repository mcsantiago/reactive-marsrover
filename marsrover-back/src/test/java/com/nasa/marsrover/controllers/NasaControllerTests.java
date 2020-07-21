package com.nasa.marsrover.controllers;

import com.nasa.marsrover.clients.nasa.NasaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NasaControllerTests {
  private NasaController controller;
  @Mock private NasaClient service;

  @BeforeEach
  private void init() {
    controller = new NasaController(service);
  }

  @Test
  public void getPicturesTest() {}
}
