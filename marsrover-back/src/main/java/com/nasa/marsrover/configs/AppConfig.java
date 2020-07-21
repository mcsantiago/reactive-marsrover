package com.nasa.marsrover.configs;

import com.nasa.marsrover.clients.nasa.NasaClient;
import com.nasa.marsrover.clients.nasa.models.NasaConfig;
import com.nasa.marsrover.services.photos.PhotoService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

  /**
   * Handle responses up to 512 KB
   *
   * @return
   */
  @Bean
  public WebClient nasaWebClient() {
    return WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs(
                    clientCodecConfigurer ->
                        clientCodecConfigurer.defaultCodecs().maxInMemorySize(512 * 1024 * 1024))
                .build())
        .baseUrl(NASA_BASE_URL)
        .build();
  }

  @Bean
  public PhotoService photoService() {
    return new PhotoService();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedOrigins("*")
            .allowedHeaders("*");
      }
    };
  }
}
