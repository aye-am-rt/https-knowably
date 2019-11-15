import { Component, OnInit } from '@angular/core';
import { ExpertRegisterService } from '../register-service/expert-register.service';
import { Router } from '@angular/router';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-expert-register',
  templateUrl: './expert-register.component.html',
  styleUrls: ['./expert-register.component.css']
})
export class ExpertRegisterComponent implements OnInit {

  constructor(private expertRegisterService:ExpertRegisterService, private route:Router) { 
    
  }
  ngOnInit() {
 
  }

  createExpert(expert){
    this.expertRegisterService.registerExpert(expert);
    this.route.navigateByUrl('/login');
  }
  dataList : any[];
  str: string = "";
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
        }
      });
    }
  }
}
