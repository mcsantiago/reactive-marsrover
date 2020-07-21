## Programming Exercise

The exercise we’d like to see is to use the NASA API described [here](https://api.nasa.gov) to build a project in GitHub that calls the Mars Rover API and selects a picture on a given day. We want your application to download and store each image locally.

Here is an <https://github.com/jlowery457/nasa-exercise | example> of this exercise done by one of our senior developers.  This is the level of effort we are looking for from someone who wants to join the LAO development team.  

### Acceptance Criteria
* Please send a link to the GitHub repo via <matt.hawkes@livingasone.com> when you are complete.
* Use list of dates below to pull the images were captured on that day by reading in a text ﬁle:
    * 02/27/17
    * June 2, 2018
    * Jul-13-2016
    * April 31, 2018
* Language needs to be *Java*.
* We should be able to run and build (if applicable) locally after you submit it
* Include relevant documentation (.MD, etc.) in the repo

### Bonus 
* Bonus - Unit Tests, Static Analysis, Performance tests or any other things you feel are important for Definition of Done
* Double Bonus - Have the app display the image in a web browser
* Triple Bonus – Have it run in a Docker or K8s (Preferable)

## Implementation

This project features a highly scalable backend featuring reactive data streams. The REST API exposes a few endpoints:

    - `GET /api/v1/nasa/getPhotoInfo?date={date}` - Where date takes on the yyyy-MM-dd format (i.e. 2020-07-21). Other formats including `07/21/2020` and `Jul-21-2020` are supported.
    - `GET /api/v1/nasa/getPhotosFromFile` - This endpoint reads a predefined dates.txt resource file described above. This is for demo purposes to showcase the flexibility of the controller and different date formats.
    - `GET /api/v1/photos/get?imgSrc={url}` - This endpoint serves the image associated with that url. The backend also caches this image in order to improve performance. 

## Installation 

To run this project locally, just call:

```
./mvnw spring-boot:run
```

For Dockerfile build (TODO: create rebuild scripts):

```
./mvnw package
docker build -t demo/marsrover .
docker run -p 8080:8080 demo/marsrover
```
