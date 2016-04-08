import React,{Component} from 'react';
import {Router, IndexRoute, Route, browserHistory} from 'react-router';
import Home from './components/home';
import AddStore from './components/addStore';
import AddPurpose from './components/addPurpose';
var injectTapEventPlugin = require("react-tap-event-plugin");
injectTapEventPlugin();

export default class App extends Component{
  render() {
    return (
      <Router history={browserHistory}>
        <Route path="/" >
          <IndexRoute component={Home} />
          <Route path="/addstore" component={AddStore} />
          <Route path="/purpose" component={AddPurpose} />
        </Route>
      </Router>
    );
  }
}
