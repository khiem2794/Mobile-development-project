import React, { Component } from 'react';
import { Link } from 'react-router'
import AppBar from 'material-ui/lib/app-bar';
import IconButton from 'material-ui/lib/icon-button';
import IconMenu from 'material-ui/lib/menus/icon-menu';
import MoreVertIcon from 'material-ui/lib/svg-icons/navigation/more-vert';
import MenuItem from 'material-ui/lib/menus/menu-item';
import Paper from 'material-ui/lib/paper';
import Menu from 'material-ui/lib/menus/menu';
import Snackbar from 'material-ui/lib/snackbar';
import Dialog from 'material-ui/lib/dialog';
import FlatButton from 'material-ui/lib/flat-button';
import RaisedButton from 'material-ui/lib/raised-button';

export default class Home extends Component {
  constructor(props){
    super(props);
    this.state = { storeID: "", brands: null, brand: null, loaded: false, delstore_open: false, delstore_msg: "", deleteconfirm_open: false };
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

  chooseBrand(brand){
    this.setState({ brand: Object.assign({}, {name: brand}, this.state.brands[brand]) })
  };

  deleteStore(storeID){
    this.setState({storeID: storeID, deleteconfirm_open: true});
  };

  deleteConfirm(){
      const store = this.state.firebase.child(this.state.brand.name+"/stores/"+this.state.storeID);
      store.remove( (err) => {
        if (!err){
          delete this.state.brand.stores[this.state.storeID];
          this.setState({ brand: this.state.brand, delstore_open: true, delstore_msg: "Remove success", storeID: "", deleteconfirm_open: false });
        } else {
          this.setState({ brand: this.state.brand, delstore_open: true, delstore_msg: "Remove failed", storeID: "", deleteconfirm_open: false });
        }
      });
  };

  render(){
    if (!this.state.loaded) {
      return (<p>loading</p>);
    }
    const actions = [
      <FlatButton
        label="Cancel"
        secondary={true}
        onTouchTap={()=>this.setState({deleteconfirm_open: false})}
      />,
      <FlatButton
        label="Yes"
        primary={true}
        keyboardFocused={true}
        onTouchTap={() => this.deleteConfirm()}
      />,
    ];
    return (
      <div className="col-xs-6">
        <AppBar
          title="HOME"
          iconElementRight={
            <IconMenu
              iconButtonElement={
                <IconButton><MoreVertIcon /></IconButton>
              }
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'top'}}
            >
              <Link to="/addstore" style={{textDecoration: "none"}}><MenuItem primaryText="Add Store" /></Link>
              <Link to="/purpose" style={{textDecoration: "none"}}><MenuItem primaryText="Purposes" /></Link>
            </IconMenu>
            }
          />
          <Paper style={{padding: "25px", marginTop: "15px"}} zDepth={4}>
            <div className="row top-xs">
              <div className="row col-xs-4 center-xs ">
                <p>Brands</p>
                <Menu>
                { Object.keys(this.state.brands).map( (v,i) => <MenuItem onClick={()=>this.chooseBrand(v)} value={v} primaryText={v} key={v} /> )}
                </Menu>
              </div>
              <div className="row col-xs-8 center-xs ">
                <p style={{display: "block"}}>Stores</p>
                { !this.state.brand ? <div></div> :
                    <Menu style={{width: "100%", marginLeft: "0"}}>
                      { Object.keys(this.state.brand.stores).map( (v,i) => <MenuItem onClick={()=>this.deleteStore(v)} value={v} primaryText={"("+this.state.brand.stores[v].lat+", "+this.state.brand.stores[v].long+") "+ this.state.brand.stores[v].address} key={v} />) }
                    </Menu> }
              </div>
              <Snackbar
                open={this.state.delstore_open}
                message={this.state.delstore_msg}
                autoHideDuration={2000}
                onRequestClose={() => this.setState({delstore_open: false})}
                action="OK"
                onActionTouchTap={() => this.setState({delstore_open: false})}
              />
              <Dialog
                title="Delete this store"
                actions={actions}
                modal={false}
                open={this.state.deleteconfirm_open}
                onRequestClose={()=>this.setState({deleteconfirm_open: false})}
              />
            </div>
          </Paper>
        </div>
      )
  }
}
