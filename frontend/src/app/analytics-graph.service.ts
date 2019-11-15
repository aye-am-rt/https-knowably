import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { analyticsgraph } from 'src/analyticsgraph';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsGraphService {
  private _url: string = "http://13.127.108.14:8099/api/v1/analytics";
  constructor(private http: HttpClient) { }
  getResponses(): Observable<analyticsgraph> {
    return this.http.get<analyticsgraph>(this._url).pipe(
      catchError((error: HttpErrorResponse) => {
        return Observable.throw(error.message || "Server Error");
      })
    )
  }
}
