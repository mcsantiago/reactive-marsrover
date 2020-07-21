package com.nasa.marsrover.clients.nasa.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RoverPicture {
  private Long id;
  private Integer sol;
  private RoverCamera camera;
  private String img_src;
  private LocalDate earth_date;
  private RoverModel rover;
}
