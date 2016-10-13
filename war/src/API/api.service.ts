import { Injectable, Inject } from '@angular/core';
import { Http, Response, Headers, RequestOptions, URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/toPromise';

/* Import our interfaces */
import { User } from './api.models';

const useMocks = false;

@Injectable()
export class PL8Service {

    constructor(private http: Http) { }

    private val: number = 14;

    urlEncode(obj: Object): string {
        let urlSearchParams = new URLSearchParams();
        for (let key in obj) {
            urlSearchParams.append(key, obj[key]);
        }
        return urlSearchParams.toString();
    }

    apiPost(url: string, body: any) {
        let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
        let options = new RequestOptions({ headers: headers });

        return this.http.post(url, this.urlEncode(body), options);
    }

    public login(username: string, password: string) {
        return this.apiPost('/login', {
            username: username,
            password: password
        })
            .toPromise()
            .catch(this.handleError)
            .then(resp => resp.json() as User);
    }

    public signup(username: string, email: string, password: string) {
        return this.apiPost('/signup', {
            username: username,
            password: password,
            email: email
        })
            .toPromise()
            .catch(this.handleError)
            .then(resp => resp.json() as User);
    }

    public logout() {
        return this.http.get("/api/auth/logout")
            .toPromise()
            .then(resp => resp.json() as {});
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    public me() {
        return this.http.get("/api/auth/me")
            .toPromise()
            .then(resp => resp.json() as User);
    }
}

@Injectable()
export class UserService {

    constructor( @Inject({PL8Service}) private PL8Service: PL8Service) {
        this.refreshUser();
    }
    
    public loadingUser: boolean = false;
    public currentUser: User = null;

    public get isLoggedIn() {
        return this.currentUser != null;
    }

    public refreshUser() {
        this.loadingUser = true;
        this.PL8Service.me()
            .catch(err => {
                this.loadingUser = false;
                this.currentUser = null;

                console.log("user not logged in");
            })
            .then(user => {
                this.loadingUser = false;
                this.currentUser = user;
            });
    }
}