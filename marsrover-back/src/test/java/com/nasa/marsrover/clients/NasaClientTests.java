package com.nasa.marsrover.clients;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.NasaConfig;
import com.nasa.marsrover.clients.nasa.models.RoverPicture;
import com.nasa.marsrover.clients.nasa.models.RoverPictureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NasaClientTests {
  NasaClient service;
  @Mock private ExchangeFunction exchangeFunction;

  @BeforeEach
  private void init() {
    WebClient client = WebClient.builder().exchangeFunction(exchangeFunction).build();
    service = new NasaClient(client, new NasaConfig("baseUrl", "apiKey"));
  }

  @Test
  public void contextLoads() {
    assertThat(service).isNotNull();
  }

  @Test
  public void getRoverPicturesSuccess() {
    when(exchangeFunction.exchange(any(ClientRequest.class)))
        .thenReturn(buildRoverPictureSuccess());

    RoverPictureResponse response = service.getRoverPictures(LocalDate.of(2020, 1, 1)).block();
    assertNotNull(response);
    assertEquals(1, response.getPhotos().size());

    RoverPicture photo = response.getPhotos().get(0);
    assertEquals(102685, photo.getId());
    assertEquals(1004, photo.getSol());
    assertEquals(20, photo.getCamera().getId());
    assertEquals("FHAZ", photo.getCamera().getName());
    assertEquals(5, photo.getCamera().getRover_id());
    assertEquals("Front Hazard Avoidance Camera", photo.getCamera().getFull_name());
    assertEquals(
        "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG",
        photo.getImg_src());
    assertEquals(LocalDate.of(2015, 6, 3), photo.getEarth_date());
    assertEquals(5, photo.getRover().getId());
    assertEquals("Curiosity", photo.getRover().getName());
    assertEquals(LocalDate.of(2012, 8, 6), photo.getRover().getLanding_date());
    assertEquals(LocalDate.of(2011, 11, 26), photo.getRover().getLaunch_date());
  }

  @Test
  public void getRoverPicturesEmpty() {
    when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(buildRoverPictureEmpty());

    RoverPictureResponse response = service.getRoverPictures(LocalDate.of(2020, 1, 1)).block();
    assertNotNull(response);
    assertEquals(0, response.getPhotos().size());
  }

  private Mono<ClientResponse> buildRoverPictureEmpty() {
    ClientResponse response =
        ClientResponse.create(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body( // Sample JSON pulled from a GET request with 404 status
                "{\"photos\": []}")
            .build();
    return Mono.just(response);
  }

  private Mono<ClientResponse> buildRoverPictureSuccess() {
    ClientResponse response =
        ClientResponse.create(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body( // Sample JSON pulled from a GET request that worked
                "{\n"
                    + "    \"photos\": [\n"
                    + "        {\n"
                    + "            \"id\": 102685,\n"
                    + "            \"sol\": 1004,\n"
                    + "            \"camera\": {\n"
                    + "                \"id\": 20,\n"
                    + "                \"name\": \"FHAZ\",\n"
                    + "                \"rover_id\": 5,\n"
                    + "                \"full_name\": \"Front Hazard Avoidance Camera\"\n"
                    + "            },\n"
                    + "            \"img_src\": \"http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG\",\n"
                    + "            \"earth_date\": \"2015-06-03\",\n"
                    + "            \"rover\": {\n"
                    + "                \"id\": 5,\n"
                    + "                \"name\": \"Curiosity\",\n"
                    + "                \"landing_date\": \"2012-08-06\",\n"
                    + "                \"launch_date\": \"2011-11-26\",\n"
                    + "                \"status\": \"active\"\n"
                    + "            }\n"
                    + "        }\n"
                    + "    ]\n"
                    + "}")
            .build();
    return Mono.just(response);
  }
}
