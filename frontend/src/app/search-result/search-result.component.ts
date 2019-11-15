import { Component, OnInit, Input } from '@angular/core';
import {UserResponseService} from '../user-response-service/user-response.service';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  resultString: string;
  result:string[];
  query: string;
  flag:boolean = true;

  likeFlag:boolean;

  constructor(private userResponseService:UserResponseService) { }

  ngOnInit() {
    this.likeFlag=true;
    this.resultString = localStorage.getItem('result');
    this.result = this.resultString.split(":");
    if(this.result.length < 2){
      this.query = "No Result Found";
      this.flag =false
    }else{
      this.query = this.result[0];
      this.result.shift();
    }
    
  }

  analyticsString:string;
  userFlag:string;
  reportFlag:boolean =true;
  userLike(query, result){
    this.likeFlag = false;
    console.log(query);
    this.analyticsString = result.join(",");
    console.log(this.analyticsString);
    this.userFlag= "accurate";
    this.userResponseService.userLike(query,this.analyticsString,this.userFlag).subscribe(
      (response) => {
        console.log("response", response);
      },
      (error: any) => {
        console.log("error", error)
      })
  }
  userReport(query, result){
    this.reportFlag = false
    console.log(query);
    this.analyticsString = result.join(",");
    console.log(this.analyticsString);
    this.userFlag= "inaccurate";
    this.userResponseService.userReport(query,this.analyticsString,this.userFlag).subscribe(
      (response) => {
        console.log("response", response);
      },
      (error: any) => {
        console.log("error", error)
      })
  }

}
