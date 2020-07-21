package com.nasa.marsrover.controllers;

import com.nasa.marsrover.services.photos.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/photos")
@Slf4j
public class PhotosController {
  private PhotoService service;

  @Autowired
  public PhotosController(PhotoService service) {
    this.service = service;
  }

  @GetMapping(value = "/get", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Mono<byte[]>> getImgUrl(@RequestParam("img_src") String imgSrc)
      throws IOException {
    Mono<byte[]> photo = service.getPhoto(imgSrc);
    HttpHeaders headers = new HttpHeaders();
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
        .headers(headers)
        .body(photo);
  }
}
