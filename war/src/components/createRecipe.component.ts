import { Component, OnInit, Input } from '@angular/core';
import { Response } from '@angular/http';
import { Router } from '@angular/router';

import { PL8Service, UserService, LocalStorageRecipeService } from '../API/api.service';
import { Recipe, Ingredient } from '../API/api.models';

@Component({
    selector: 'my-createRecipe',
    templateUrl: '/templates/createRecipe.html',
})
export class CreateRecipeComponent implements OnInit {

    constructor(
        private router: Router,
        private PL8Service: PL8Service,
        private recipeStorage: LocalStorageRecipeService
    ) {}

    public name: string;
    public description: string;
    public isLoading: boolean;
    @Input() public errorMessage: string;

    public recipe: Recipe = {
        key: {
            kind: "Recipe",
            id: -1
        },
        propertyMap: {
            Name: "",
            Description: "",
            Ingredients: [],
            Steps: [],
            Pic: ""
        }

    };

    public onSubmit() {
        this.isLoading = true;

        this.recipeStorage.createRecipe(this.recipe);
        this.router.navigate(['/home']);
        return false;
        } 
    
    ngOnInit(): void {
        this.addIng();
        this.addStep();
    }


    addIng() {
        this.recipe.propertyMap.Ingredients.push(<Ingredient>{
            ingredient: "",
            amount: 0,
            unit: ""
        });
    }

    addStep() {
        if (this.recipe.propertyMap.Steps.length > 0) {
            var inputValue = (<HTMLInputElement>document.getElementById("info")).value;
            console.log("Step = ", inputValue);
            this.recipe.propertyMap.Steps.push(inputValue);
        }
        else {
            this.recipe.propertyMap.Steps.push("");
        }
    }
}
