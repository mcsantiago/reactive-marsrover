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
      startDate: new Date()
    };

    this.handleChange = this.handleChange.bind(this);
    this.onFormSubmit = this.onFormSubmit.bind(this);
  }

  handleChange(date) {
    this.setState({
      startDate: date
    })
  }

  async onFormSubmit(e) {
    try {
      console.log(formatDate(this.state.date));
      const response = await axios.get('localhost:8080/picture', {
        params: {
          date: formatDate(this.state.startDate),
          hd: false
        }
      }).then(
        () => console.log(response)
      );
      this.PHOTO_SET = response.data;
    } catch (error) {
      console.error(error);
    }
    e.preventDefaultBehavior();
    console.log(this.state.startDate)
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

        <h1>Curiousity</h1>
        <Gallery photos={this.PHOTO_SET} />
      </div>
    );
  }

}

export default App;