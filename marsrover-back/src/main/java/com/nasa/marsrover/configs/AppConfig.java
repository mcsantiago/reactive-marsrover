package com.nasa.marsrover.configs;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.NasaConfig;
import com.nasa.marsrover.services.photos.PhotoService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
  /*
  TODO: For demonstration purposes, API key is left here. In actual production, please move this API key out
  into a hidden configuration file
  */
  private static final String NASA_BASE_URL = "https://api.nasa.gov/";
  private static final String NASA_API_KEY = "J93VkSYfzLnIy74qXdtIUh3tcjfToNux8vV3bHct";

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public NasaClient nasaClient() {
    return new NasaClient(nasaWebClient(), nasaConfig());
  }

  @Bean
  public NasaConfig nasaConfig() {
    return new NasaConfig(NASA_BASE_URL, NASA_API_KEY);
  }

  @Bean
  public WebClient nasaWebClient() {
    return WebClient.create(NASA_BASE_URL);
  }

  @Bean
  public PhotoService photoService() {
    return new PhotoService();
  }
}
