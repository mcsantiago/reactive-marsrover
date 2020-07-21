package com.nasa.marsrover.controllers;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import com.nasa.marsrover.utils.DateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/** Nasa endpoint controller */
@RestController
@RequestMapping("/api/v1/nasa")
@Slf4j
public class NasaController {
  private NasaClient nasaClient;

  @Autowired
  public NasaController(NasaClient nasaClient) {
    this.nasaClient = nasaClient;
  }

  @GetMapping("getPhotoInfo")
  public Mono<RoverPictureResponse> getPhotos(@RequestParam("date") String date) {
    return nasaClient.getRoverPictures(DateParser.parseDate(date));
  }

  /**
   * Loads the resource file and returns a stream of rover picture responses
   *
   * @return
   * @throws IOException
   */
  @GetMapping("getPhotosFromFile")
  public Flux<RoverPictureResponse> getPhotosFromFile() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("dates.txt");
    return nasaClient.getRoverPictureFromFile(inputStream);
  }
}
