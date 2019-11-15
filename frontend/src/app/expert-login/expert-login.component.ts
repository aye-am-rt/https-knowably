import { Component, OnInit } from '@angular/core';
import { ExpertLoginService } from '../login-service/expert-login.service';
import { experts } from '../../expert';
import { Router } from '@angular/router';
import { ExpertService } from '../expert_service/expert.service';

@Component({
  selector: 'app-expert-login',
  templateUrl: './expert-login.component.html',
  styleUrls: ['./expert-login.component.css']
})
export class ExpertLoginComponent implements OnInit {

  constructor(private expertLoginService:ExpertLoginService,private route: Router, private _expertService:ExpertService) { }

  ngOnInit() {
  }

  loginExpert(expert){
    this.expertLoginService.loginExpert(expert).subscribe(data=>{
      localStorage.setItem('token', data.token);
      //this._expertService.setJwtToken(data.token);
      this.route.navigateByUrl('/expert-dashboard');
    },error=>{
      console.log(error);
      this.route.navigateByUrl('/register');
    });
    
  }

}
