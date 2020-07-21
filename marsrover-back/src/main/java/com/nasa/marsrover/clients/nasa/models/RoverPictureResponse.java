package com.nasa.marsrover.clients.nasa.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoverPictureResponse {
  List<RoverPicture> photos;
}
