import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import Gallery from 'react-photo-gallery';
import axios from 'axios';
import formatDate from './utils/DateFormatter';

import "./App.css";
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.min.css';

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      startDate: new Date(),
      photoSet: []
    };

    this.handleChange = this.handleChange.bind(this);
    this.onFormSubmit = this.onFormSubmit.bind(this);

    axios.get('http://localhost:8080/api/v1/nasa/getPhotoInfo', {
      params: {
        date: formatDate(this.state.startDate)
      },
      headers: {
      }
    }).then(
      res => res.data
    ).then(data => {
      let photos = [];
      console.log(data);
      for (let index = 0; index < data.photos.length; index++) {
        let photo = {
          src: "http://localhost:8080/api/v1/photos/get?img_src=" + data.photos[index].img_src,
          width: 4,
          height: 3
        };
        photos.push(photo);
      }
      this.setState({
        photoSet: photos
      })
    }).catch((error) => console.error(error));
  }


  async handleChange(date) {
    this.setState({
      startDate: date
    });
    const response = await axios.get('http://localhost:8080/api/v1/nasa/getPhotoInfo', {
      params: {
        date: formatDate(this.state.startDate)
      },
      headers: {
      }
    }).then(
      res => res.data
    ).then(data => {
      let photos = [];
      console.log(data);
      for (let index = 0; index < data.photos.length; index++) {
        let photo = {
          src: "http://localhost:8080/api/v1/photos/get?img_src=" + data.photos[index].img_src,
          width: 4,
          height: 3
        };
        photos.push(photo);
      }
      this.setState({
        photoSet: photos
      })
    }).catch((error) => console.error(error));
  }

  async onFormSubmit(e) {
    e.preventDefaultBehavior();
    console.log(this.state.startDate);
  }

  PHOTO_SET = [
    // {
    //   src: 'http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG',
    //   width: 4,
    //   height: 3
    // },
    // {
    //   src: 'http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG',
    //   width: 4,
    //   height: 3
    // },
    // {
    //   src: 'http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG',
    //   width: 4,
    //   height: 3
    // },
  ];


  render() {
    return (
      <div className="App">
        <h1>Mars Rover Photo Album</h1>
        <p>Ever wonder what the NASA rovers are doing on Mars? Turns out, the rovers have been keeping a daily image diary!</p>
        <form onSubmit={this.onFormSubmit}>
          <div className="form-group">
            <DatePicker
              selected={this.state.startDate}
              onChange={this.handleChange}
              name="startDate"
              dateFormat="MM/dd/yyyy"
            />
            <button className="btn btn-primary">Find Pictures!</button>
          </div>
        </form>

        <h2>Curiosity Photos</h2>
        <Gallery photos={this.state.photoSet} />
      </div>
    );
  }

}

export default App;