package com.nasa.marsrover.clients.nasa;

import com.nasa.marsrover.clients.nasa.models.NasaConfig;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/** Service class responsible for making calls to the Nasa API */
@Slf4j
public class NasaClient {
  private WebClient client;
  private NasaConfig config;

  @Autowired
  public NasaClient(WebClient client, NasaConfig config) {
    this.client = client;
    this.config = config;
  }

  /**
   * GET https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos
   *
   * @param date
   * @return
   */
  public Mono<RoverPictureResponse> getRoverPictures(LocalDate date) {
    return client
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("mars-photos/api/v1/rovers/curiousity/photos")
                    .queryParam("earth_date", date.toString())
                    .queryParam("api_key", config.getApiKey())
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError, // TODO: Handle internal server error
            response ->
                Mono.error(
                    WebClientResponseException.create(
                        response.statusCode().value(),
                        response.statusCode().getReasonPhrase(),
                        response.headers().asHttpHeaders(),
                        null,
                        null,
                        null)))
        .bodyToMono(RoverPictureResponse.class)
        .doOnError(
            WebClientResponseException.class,
            ex -> log.error(ex.getStatusCode() + ": " + ex.getStatusText()))
        .log();
  }

  /**
   * Obtains the image file from URL
   *
   * @param url
   * @return
   */
  public Flux<DataBuffer> getPhoto(String url) {
    WebClient client =
        WebClient.builder()
            .clientConnector(
                new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
            .baseUrl(url)
            .build();

    return client.get().accept(MediaType.APPLICATION_XML).retrieve().bodyToFlux(DataBuffer.class);
  }

  /**
   * Reads the dates.txt file and streams the
   *
   * @return
   * @throws IOException
   */
  public Flux<RoverPictureResponse> getRoverPictureFromFile() throws IOException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream("dates.txt");
    InputStreamReader streamReader =
        new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);
    return Flux.fromStream(
        Stream.generate(
            new Supplier<RoverPictureResponse>() {
              @SneakyThrows
              @Override
              public RoverPictureResponse get() {
                String line;
                while ((line = reader.readLine()) != null) {
                  // Process line
                  // TODO:
                }
                return null;
              }
            }));
  }
}
