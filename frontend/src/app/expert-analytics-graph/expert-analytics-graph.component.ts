import { Component, OnInit, OnChanges } from '@angular/core';
import * as CanvasJS from '../../assets/canvasjs.min';
import { AnalyticsGraphService } from '../analytics-graph.service';

@Component({
  selector: 'app-expert-analytics-graph',
  templateUrl: './expert-analytics-graph.component.html',
  styleUrls: ['./expert-analytics-graph.component.css']
})
export class ExpertAnalyticsGraphComponent implements OnInit {
  public analytics;
  public errorMsg;
  constructor(private _analyticsgraph: AnalyticsGraphService) { }

  getAnalytics() {
    return this._analyticsgraph.getResponses()
  }
  ngOnInit() {
    this._analyticsgraph.getResponses()
                        .subscribe(data => this.analytics = data,
                                   error => this.errorMsg = error);
  }
  keyClick(){
    let chart = new CanvasJS.Chart("chartContainer", {
      theme: "light2",
      animationEnabled: true,
      exportEnabled: true,
      title:{
        text: "Negative Responses"
      },
      data: [{
        type: "pie",
        showInLegend: true,
        toolTipContent: "<b>{name}</b>: {y} (#percent%)",
        indexLabel: "{name} - #percent%",
        dataPoints: [
          { y: this.analytics.movNegResp, name: "Movie" },
          { y: this.analytics.medNegResp, name: "Medical" }
        ]
      }]
    });
      
    chart.render();
    let chart2 = new CanvasJS.Chart("chartContainer2", {
      theme: "light2",
      animationEnabled: true,
      exportEnabled: true,
      title:{
        text: "Positive Responses"
      },
      data: [{
        type: "pie",
        showInLegend: true,
        toolTipContent: "<b>{name}</b>: {y} (#percent%)",
        indexLabel: "{name} - #percent%",
        dataPoints: [
          { y: this.analytics.movPosResp, name: "Movie" },
          { y: this.analytics.medPosResp, name: "Medical" }
        ]
      }]
    });
      
    chart2.render();
    let chart3 = new CanvasJS.Chart("chartContainer3", {
      theme: "light2",
      animationEnabled: true,
      exportEnabled: true,
      title:{
        text: "Accuracy"
      },
      data: [{
        type: "pie",
        showInLegend: true,
        toolTipContent: "<b>{name}</b>: {y}% (#percent%)",
        indexLabel: "{name} - #percent%",
        dataPoints: [
          { y: this.analytics.movAcc, name: "Movie" },
          { y: this.analytics.medAcc, name: "Medical" }
        ]
      }]
    });
      
    chart3.render();
  }
}
