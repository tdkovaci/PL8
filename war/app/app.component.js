"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var api_service_1 = require("./API/api.service");
/* This is the app component typescript (.ts) file.  This creates the main App Component, or the root component */
var AppComponent = (function () {
    function AppComponent(PL8Service, storageService, pantryService, router, UserService) {
        this.PL8Service = PL8Service;
        this.storageService = storageService;
        this.pantryService = pantryService;
        this.router = router;
        this.UserService = UserService;
    }
    AppComponent.prototype.logOut = function () {
        this.PL8Service.logout();
        this.isLoggedIn = false;
        this.router.navigate(['/home']);
    };
    AppComponent.prototype.ngOnInit = function () {
        if (sessionStorage.getItem("currentUser") != null) {
            this.isLoggedIn = true;
        }
        else {
            this.isLoggedIn = false;
        }
    };
    return AppComponent;
}());
AppComponent = __decorate([
    core_1.Component({
        selector: 'my-app',
        templateUrl: 'templates/app.html',
        providers: [api_service_1.PL8Service, api_service_1.LocalStorageRecipeService, api_service_1.LocalStoragePantryService]
    }),
    __param(4, core_1.Input()),
    __metadata("design:paramtypes", [api_service_1.PL8Service,
        api_service_1.LocalStorageRecipeService,
        api_service_1.LocalStoragePantryService,
        router_1.Router,
        api_service_1.UserService])
], AppComponent);
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map