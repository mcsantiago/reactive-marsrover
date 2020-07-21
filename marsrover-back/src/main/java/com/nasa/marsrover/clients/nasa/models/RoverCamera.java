package com.nasa.marsrover.clients.nasa.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoverCamera {
  private Integer id;
  private String name;
  private Integer rover_id;
  private String full_name;
}
