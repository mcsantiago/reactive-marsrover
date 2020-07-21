package com.nasa.marsrover.controllers;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NasaControllerTests {
  private NasaController controller;
  @Mock private NasaClient nasaClient;

  @BeforeEach
  private void init() {
    controller = new NasaController(nasaClient);
  }

  @Test
  public void getPhotoInfoSuccess() {
    RoverPictureResponse response = new RoverPictureResponse();
    when(nasaClient.getRoverPictures(any(LocalDate.class))).thenReturn(Mono.just(response));
    assertNotNull(controller.getPhotoInfo("2020-1-1"));
  }

  @Test
  public void getPhotosFromFile() throws IOException {
    RoverPictureResponse response1 = new RoverPictureResponse();
    RoverPictureResponse response2 = new RoverPictureResponse();
    when(nasaClient.getRoverPictureFromFile(any(InputStream.class)))
        .thenReturn(Flux.just(response1, response2));
    assertNotNull(controller.getPhotosFromFile());
  }
}
