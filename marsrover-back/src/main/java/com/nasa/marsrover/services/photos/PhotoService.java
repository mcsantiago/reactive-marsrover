package com.nasa.marsrover.services.photos;

import com.nasa.marsrover.clients.nasa.NasaClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.*;
import java.util.Objects;

@Slf4j
public class PhotoService {
  @Autowired private NasaClient nasaClient;

  /**
   * Obtains the photo provided by the URL
   *
   * @param imgSrc
   * @return
   * @throws IOException
   */
  public Mono<byte[]> getPhoto(String imgSrc) throws IOException {
    final String sha = DigestUtils.sha512Hex(imgSrc);
    final String directoryName =
        Objects.requireNonNull(getClass().getClassLoader().getResource(".")).getFile() + "images/";
    final String imageFileName = // Cache the file in resources folder
        directoryName + sha + ".jpg";

    File directory = new File(directoryName);
    if (!directory.exists()) {
      directory.mkdir();
    }

    File file = new File(imageFileName);

    if (file.createNewFile()) {
      log.trace("File {} NOT found in cache, requesting from {}", imageFileName, imgSrc);
      AsynchronousFileChannel asynchronousFileChannel =
          AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.WRITE);
      DataBufferUtils.write(nasaClient.getPhotoBuffer(imgSrc), asynchronousFileChannel)
          .doOnNext(DataBufferUtils.releaseConsumer())
          .doAfterTerminate(
              () -> {
                try {
                  asynchronousFileChannel.close();
                } catch (IOException ignored) {
                }
              })
          .subscribe();
      return nasaClient.getPhotoBytes(imgSrc);
    } else {
      log.trace("File {} found in cache", imageFileName);
      return Mono.just(Files.readAllBytes(file.toPath()));
    }
  }
}
