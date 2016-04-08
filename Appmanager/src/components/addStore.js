import React, { Component } from 'react';
import { Link } from 'react-router';
import Firebase from 'firebase';
import AppBar from 'material-ui/lib/app-bar';
import IconButton from 'material-ui/lib/icon-button';
import IconMenu from 'material-ui/lib/menus/icon-menu';
import MoreVertIcon from 'material-ui/lib/svg-icons/navigation/more-vert';
import MenuItem from 'material-ui/lib/menus/menu-item';
import TextField from 'material-ui/lib/text-field';
import RaisedButton from 'material-ui/lib/raised-button';
import Paper from 'material-ui/lib/paper';
import SelectField from 'material-ui/lib/select-field';
import Snackbar from 'material-ui/lib/snackbar';

export default class AddStore extends Component {
  constructor(props){
    super(props);
    this.state = { brands: null, brand: null, loaded: false, addstore_check: false, addstore_msg: ""};
  }

  chooseBrand = (event, index, value) => {
    this.setState({brand: value});
  };

  submitStore = () => {
    if (this._long.refs.input.value && this._lat.refs.input.value && this._address.refs.input.value && this.state.brand && this._district.refs.input.value && this._city.refs.input.value) {
      const brand = this.state.firebase.child(this.state.brand+"/stores");
      brand.push().set({lat: this._lat.refs.input.value, long: this._long.refs.input.value, address: this._address.refs.input.value, district: this._district.refs.input.value, city: this._city.refs.input.value});
      this.setState({addstore_check: true, addstore_msg: "Push successful"});
      this._long.refs.input.value = "";
      this._lat.refs.input.value  = "";
      this._address.refs.input.value = "";
      this._district.refs.input.value = "";
      this._city.refs.input.value = "";
    } else {
      this.setState({addstore_check: true, addstore_msg: "Wrong input fields"});
    }
  };

  componentDidMount(){
    if (!this.state.loaded){
      const firebase = new Firebase("https://storefinder.firebaseio.com/").child("Brands");
      const self = this;
      firebase.on("value", function(snapshot) {
        console.log(snapshot.val());
        self.setState({brands: snapshot.val(), firebase: firebase,loaded: true});
      }).bind(self);
    }
  };

  render(){
    if (!this.state.loaded) {
      return <p>loading</p>
    }
    return (
      <div className="col-xs-6">

        <AppBar
          className=""
          title="ADD STORE"
          iconElementRight={
            <IconMenu
              iconButtonElement={
                <IconButton><MoreVertIcon /></IconButton>
              }
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'top'}}
            >
              <Link style={{textDecoration: "none"}} to="/"><MenuItem primaryText="Home" /></Link>
              <Link style={{textDecoration: "none"}} to="/purpose"><MenuItem primaryText="Purposes" /></Link>

            </IconMenu>
            }
        />
        <Paper style={{padding: "25px", marginTop: "15px"}} zDepth={4}>
          <div className="row center-xs">
            <div className="col-xs-8">
              <TextField style={{width: "100%"}} floatingLabelText="Store Latitude" ref={(input) => this._lat = input}/><br />
              <TextField style={{width: "100%"}} floatingLabelText="Store Longtitude" ref={(input) => this._long = input}/><br />
              <TextField style={{width: "100%"}} floatingLabelText="Store Address" ref={(input) => this._address = input}/><br />
              <TextField style={{width: "100%"}} floatingLabelText="Store District" ref={(input) => this._district = input}/><br />
              <TextField style={{width: "100%"}} floatingLabelText="Store City" ref={(input) => this._city = input}/><br />
              <div className="end-xs">
                <SelectField value={this.state.brand} onChange={this.chooseBrand} hintText="Choose Brand">
                  { Object.keys(this.state.brands).map( (v,i) => <MenuItem value={v} primaryText={v} key={v} /> )}
                </SelectField>
              </div>
              <div className="row end-xs">
                <RaisedButton label="Submit" primary={true} style={{}} onClick={this.submitStore}/>
                <Snackbar
                  open={this.state.addstore_check}
                  message={this.state.addstore_msg}
                  autoHideDuration={2000}
                  onRequestClose={() => this.setState({addstore_check: false})}
                  action="OK"
                  onActionTouchTap={() => this.setState({addstore_check: false})}
                />
              </div>
            </div>
          </div>
        </Paper>
      </div>
    )
  }
}
