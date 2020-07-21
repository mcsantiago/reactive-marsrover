package com.nasa.marsrover.clients.nasa;

import com.nasa.marsrover.clients.nasa.models.NasaConfig;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import com.nasa.marsrover.utils.DateParser;
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
import java.util.ArrayList;
import java.util.Objects;

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
                    .path("mars-photos/api/v1/rovers/curiosity/photos")
                    .queryParam("earth_date", date.toString())
                    .queryParam("api_key", config.getApiKey())
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError, // TODO: Handle internal server error (status code 5xx)
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
  public Mono<byte[]> getPhotoBytes(String url) {
    WebClient client =
        WebClient.builder()
            .clientConnector(
                new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
            .baseUrl(url)
            .build();

    return client.get().accept(MediaType.APPLICATION_XML).retrieve().bodyToMono(byte[].class).log();
  }

  public Flux<DataBuffer> getPhotoBuffer(String url) {
    WebClient client =
        WebClient.builder()
            .clientConnector(
                new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
            .baseUrl(url)
            .build();

    return client
        .get()
        .accept(MediaType.APPLICATION_XML)
        .retrieve()
        .bodyToFlux(DataBuffer.class)
        .log();
  }

  /**
   * Reads the dates.txt file and streams the responses
   *
   * @return
   * @throws IOException
   */
  public Flux<RoverPictureResponse> getRoverPictureFromFile(InputStream inputStream)
      throws IOException {
    InputStreamReader streamReader =
        new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);
    String line;
    ArrayList<Mono<RoverPictureResponse>> responseCollection = new ArrayList<>();
    while ((line = reader.readLine()) != null) {
      try {
        LocalDate date = DateParser.parseDate(line);
        responseCollection.add(getRoverPictures(date));
      } catch (IllegalArgumentException ignore) {
      }
    }

    return Flux.concat(responseCollection);
  }
}
