"use strict";
var router_1 = require('@angular/router');
var login_component_1 = require('./components/login.component');
var signup_component_1 = require('./components/signup.component');
var home_component_1 = require('./components/home.component');
var appRoutes = [
    {
        path: 'login', component: login_component_1.LoginComponent
    },
    {
        path: 'signup', component: signup_component_1.SignupComponent
    },
    {
        path: 'home', component: home_component_1.HomeComponent
    }
];
exports.routing = router_1.RouterModule.forRoot(appRoutes, { useHash: true });
//# sourceMappingURL=app.routing.js.map