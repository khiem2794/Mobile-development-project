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
import List from 'material-ui/lib/lists/list';
import ListItem from 'material-ui/lib/lists/list-item';
import Divider from 'material-ui/lib/divider';

export default class AddStore extends Component {
  constructor(props){
    super(props);
    this.state = { brands: null, brand: null, loaded: false };
  }

  chooseBrand = (event, index, value) => {
    this.setState( { brand: Object.assign({}, {name: value}, this.state.brands[value]) } );
    console.log(this.state.brands[value]);
  };

  submitPurpose = () => {
    if (this._purpose.refs.input.value && this.state.brand){
      const purposes = this.state.firebase.child(this.state.brand.name+"/purposes");
      purposes.push().set({name: this._purpose.refs.input.value});
    }
  };

  componentDidMount(){
    if (!this.state.loaded){
      const firebase = new Firebase("https://storefinder.firebaseio.com/").child("Brands");
      const self = this;
      firebase.on("value", function(snapshot) {
        console.log(snapshot.val());
        if (self.state.brand) {
          self.setState({brands: snapshot.val(), brand: Object.assign({name: self.state.brand.name}, snapshot.val()[self.state.brand.name]), firebase: firebase, loaded: true});
        } else {
          self.setState({brands: snapshot.val(), firebase: firebase, loaded: true});
        }
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
          title="STORE PURPOSES"
          iconElementRight={
            <IconMenu
              iconButtonElement={
                <IconButton><MoreVertIcon /></IconButton>
              }
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'top'}}
            >
              <Link style={{textDecoration: "none"}} to="/"><MenuItem primaryText="Home" /></Link>
              <Link style={{textDecoration: "none"}} to="/addstore"><MenuItem primaryText="Add Store" /></Link>

            </IconMenu>
            }
        />
        <Paper style={{padding: "25px", marginTop: "15px"}} zDepth={4}>
          <div className="row center-xs">

            <div className="col-xs-4">
              <List>
                { !this.state.brand ? <p>Pick a Brand</p> :
                  <div>
                    <p>{this.state.brand.name}</p>
                    <Divider />
                    { Object.keys(this.state.brand.purposes).map( (v,i) => <div key={v}><ListItem primaryText={this.state.brand.purposes[v].name} /><Divider /></div> ) }
                  </div>
                }
              </List>
            </div>

            <div className="col-xs-8">
              <TextField style={{width: "100%"}} floatingLabelText="Store Purpose" ref={(input) => this._purpose = input}/><br />
              <div className="end-xs">
                <SelectField value={ !this.state.brand ? "" : this.state.brand.name } onChange={this.chooseBrand} hintText="Choose Brand">
                  { Object.keys(this.state.brands).map( (v,i) => <MenuItem value={v} primaryText={v} key={v} /> )}
                </SelectField>
              </div>
              <div className="row end-xs">
                <RaisedButton label="Submit" primary={true} style={{}} onClick={this.submitPurpose}/>
              </div>
            </div>

          </div>
        </Paper>
      </div>
    )
  }
}
