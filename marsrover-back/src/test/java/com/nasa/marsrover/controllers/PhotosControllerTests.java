package com.nasa.marsrover.controllers;

import com.nasa.marsrover.services.photos.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PhotosControllerTests {
  private PhotosController photosController;
  @Mock PhotoService service;

  @BeforeEach
  private void init() {
    photosController = new PhotosController(service);
  }

  @Test
  public void getImgUrl() throws IOException {
    byte[] data = new byte[] {0x01, 0x02};
    when(service.getPhoto(any(String.class)))
        .thenAnswer((Answer<Mono<byte[]>>) invocationOnMock -> null);
    ResponseEntity<Mono<byte[]>> response = photosController.getImgUrl("test");
    assertNotNull(response);
  }
}
