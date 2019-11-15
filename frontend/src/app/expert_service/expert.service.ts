import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { expertInput } from 'src/expertInput';
import {expertDTO} from 'src/expertDTO';



@Injectable({
  providedIn: 'root'
})
export class ExpertService {

  private _expertDTO: expertDTO;
  private token: any;
  constructor(private http:HttpClient) { }

   loginExpert(expert:expertInput, userId:String):any{
     console.log('from dashboard'+ this.token);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Authorization': 'Bearer '+ localStorage.getItem('token')
       })
    };
    let domain = expert.domain;
    let concept = expert.concept;
    this._expertDTO = {
      userId,
      domain,
      concept
    };
    console.log("*****this.....",this._expertDTO);
    let url = "https://knowably.stackroute.io:8080/google-search/api/v1/domain";
    return this.http.post<any>(url, this._expertDTO, httpOptions);
    //     let post_url = `http://localhost:8080/user-service/api/v1/user`;
    // return this.http.post(post_url,newUser,httpOptions);
  }

  // setJwtToken(token){
  //     this.token = token;
  //     console.log(this.token);
  // }
}
