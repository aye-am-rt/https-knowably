import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { registerExperts } from 'src/register-expert';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ExpertRegisterService {

  constructor(private http:HttpClient) { }

  registerExpert(expert:registerExperts):any{
    console.log(expert);
    
        //this.http.post();
  }
}
