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
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var api_service_1 = require("../API/api.service");
var HomeComponent = (function () {
    function HomeComponent(router, Pl8Service) {
        this.router = router;
        this.Pl8Service = Pl8Service;
    }
    HomeComponent.prototype.ngOnInit = function () {
        this.isLoading = true;
    };
    HomeComponent.prototype.showAll = function () {
        this.recipes = this.allRecipes;
        return false;
    };
    return HomeComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", Boolean)
], HomeComponent.prototype, "isLoading", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", Array)
], HomeComponent.prototype, "recipes", void 0);
HomeComponent = __decorate([
    core_1.Component({
        selector: 'my-home',
        templateUrl: '/templates/home.html',
    }),
    __metadata("design:paramtypes", [router_1.Router,
        api_service_1.PL8Service])
], HomeComponent);
exports.HomeComponent = HomeComponent;
//# sourceMappingURL=home.component.js.map