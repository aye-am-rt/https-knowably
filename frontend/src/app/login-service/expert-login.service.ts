import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { experts } from '../../expert';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ExpertLoginService {
  private userId:any;
  constructor(private http:HttpClient) { }

  loginExpert(expert:experts):any{
    console.log(expert);
    this.userId = expert.name;
    localStorage.setItem('user', this.userId);
    let url = "https://knowably.stackroute.io:8080/user-service/api/v1/authenticate";
    return this.http.post(url, expert, httpOptions);
  }

  getUserId(){
    return this.userId;
  }
}




