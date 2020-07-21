package com.nasa.marsrover.clients.nasa.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NasaConfig {
  private String baseUrl;
  private String apiKey;
}
