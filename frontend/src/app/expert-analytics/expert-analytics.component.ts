import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../analytics-service/analytics.service';


@Component({
  selector: 'app-expert-analytics',
  templateUrl: './expert-analytics.component.html',
  styleUrls: ['./expert-analytics.component.css']
})
export class ExpertAnalyticsComponent implements OnInit {

  public responses =  [];
  public errorMsg;
  public displayedColumns: string[] = ['domain','query','result','posResponse','negResponse'];
  constructor(private _analytics: AnalyticsService) { }

  ngOnInit() {
    this._analytics.changeURL("http://13.127.108.14:8099/api/v1/display");
    this._analytics.getResponses()
        .subscribe(data => this.responses=data,
                   error => this.errorMsg = error);
  }
}
