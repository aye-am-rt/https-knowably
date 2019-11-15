import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../analytics-service/analytics.service';

@Component({
  selector: 'app-expert-analytics-movie',
  templateUrl: './expert-analytics-movie.component.html',
  styleUrls: ['./expert-analytics-movie.component.css']
})
export class ExpertAnalyticsMovieComponent implements OnInit {
  public responses =  [];
  public errorMsg;
  public displayedColumns: string[] = ['domain','query','result','posResponse','negResponse'];
  constructor(private _analytics2: AnalyticsService) { }

  ngOnInit() {
    this._analytics2.changeURL("http://13.127.108.14:8099/api/v1/display/movie");
    this._analytics2.getResponses()
        .subscribe(data => this.responses=data,
                   error => this.errorMsg = error);
  }

}
