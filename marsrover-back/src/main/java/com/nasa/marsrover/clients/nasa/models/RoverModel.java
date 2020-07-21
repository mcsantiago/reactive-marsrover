package com.nasa.marsrover.clients.nasa.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RoverModel {
  private Integer id;
  private String name;
  private LocalDate landing_date;
  private LocalDate launch_date;
  private String status;
}
