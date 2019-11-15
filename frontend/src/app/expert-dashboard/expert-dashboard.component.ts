import { Component, OnInit } from '@angular/core';
import { ExpertService } from '../expert_service/expert.service';
import { Router } from '@angular/router';
import { ExpertLoginService } from '../login-service/expert-login.service';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-expert-dashboard',
  templateUrl: './expert-dashboard.component.html',
  styleUrls: ['./expert-dashboard.component.css']
})
export class ExpertDashboardComponent implements OnInit {

  constructor(private _expertService: ExpertService, private route: Router, private _expertLoginService: ExpertLoginService) { }
  private userId: any;
  private str: string = "";
  ngOnInit() {
    //this.userId = this._expertLoginService.getUserId();
    this.userId = localStorage.getItem('user');
    console.log("from dashboard" + this.userId);
  }
  inputData(expertInput): any {
    if(expertInput.concept == ""){
      expertInput.concept = this.str;
    }
    this._expertService.loginExpert(expertInput, this.userId)
      .subscribe(
        (response) => {
          console.log("response", response)
          this.route.navigateByUrl('');
        },
        (error: any) => {
          console.log("error", error)
        })
  }

  doLogout(){
    localStorage.clear();
  }

  dataList : any[];
  onChange(files: File[]){
    if(files[0]){
      console.log(files[0]);
      Papa.parse(files[0], {
        header: true,
        skipEmptyLines: true,
        complete: (result,file) => {
          for (let entry of result.data) {
            console.log('entry', entry.cancer);
            this.str = this.str + entry.cancer+",";
        }
        console.log("from file    "+this.str.substr(0, this.str.length-1));
          this.dataList = result.data;
          this.str = this.str.substr(0,this.str.length-1);
        }
      });
    }
  }

}
