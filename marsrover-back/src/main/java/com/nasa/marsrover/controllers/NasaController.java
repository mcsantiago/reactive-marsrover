package com.nasa.marsrover.controllers;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/** Nasa endpoint controller */
@RestController
@RequestMapping("/nasa/v1")
@Slf4j
public class NasaController {
  private NasaClient nasaClient;

  @Autowired
  public NasaController(NasaClient nasaClient) {
    this.nasaClient = nasaClient;
  }

  @GetMapping("getPhotos")
  public Mono<RoverPictureResponse> getPhotos(@RequestParam("date") LocalDate date) {
    return nasaClient.getRoverPictures(date);
  }

  @GetMapping("getPhotosFromFile")
  public Flux<RoverPictureResponse> getPhotosFromFile() {
    return nasaClient.getRoverPictureFromFile();
  }
}
