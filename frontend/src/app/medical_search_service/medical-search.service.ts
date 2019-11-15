import { Injectable } from '@angular/core';
import {searchQuery} from '../../searchQuery';
import { HttpHeaders, HttpClient } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class MedicalSearchService {

  constructor(private http:HttpClient) { }
   userSearchService(search:searchQuery){
      search.domain = "medical";
      console.log("service "+search.searchTerm);
      return this.http.post("https://knowably.stackroute.io:8080/queryservice/api/v1/query", search, httpOptions)
    }
}
