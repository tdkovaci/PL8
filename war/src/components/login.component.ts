import { Component, OnInit, Input } from '@angular/core';
import { Response } from '@angular/http';
import { Router } from '@angular/router';

@Component({
  selector: 'my-login',
  templateUrl: '/templates/login.html',
})

export class LoginComponent implements OnInit {
         constructor(
    private router: Router) {}
        
       ngOnInit(): void {
       
       }
}
 
