import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../analytics-service/analytics.service';

@Component({
  selector: 'app-expert-analytics-medical',
  templateUrl: './expert-analytics-medical.component.html',
  styleUrls: ['./expert-analytics-medical.component.css']
})
export class ExpertAnalyticsMedicalComponent implements OnInit {
  public responses =  [];
  public errorMsg;
  public displayedColumns: string[] = ['domain','query','result','posResponse','negResponse'];
  constructor(private _analytics1: AnalyticsService) { }

  ngOnInit() {
    this._analytics1.changeURL("http://13.127.108.14:8099/api/v1/display/medical");
    this._analytics1.getResponses()
        .subscribe(data => this.responses=data,
                   error => this.errorMsg = error);
  }

}
