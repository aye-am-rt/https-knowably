import { Injectable } from '@angular/core';
import {UserResultResponse} from '../../userResultResponse';
import { HttpHeaders, HttpClient } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':'application/json'
   })
};
@Injectable({
  providedIn: 'root'
})
export class UserResponseService {
  domain1:string;
  constructor(private http:HttpClient) {
    this.domain1 = localStorage.getItem('domain');
   }
  userResultResponse:UserResultResponse;
  
  userLike(query,result,response){
    const domain = this.domain1; 
    this.userResultResponse = {
      query,
      result,
      response,
      domain
    };
    console.log(this.userResultResponse);
    return this.http.post<any>("http://13.127.108.14:8099/api/v1/response", this.userResultResponse, httpOptions);
  }
  userReport(query,result,response){
    const domain = this.domain1; 
    this.userResultResponse = {
      query,
      result,
      response,
      domain
    };
    console.log(this.userResultResponse);
    return this.http.post<any>("http://13.127.108.14:8099/api/v1/response", this.userResultResponse, httpOptions);
  }
}
