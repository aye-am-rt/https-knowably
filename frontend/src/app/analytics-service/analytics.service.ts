import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { analytics } from 'src/analytics';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private _url: string = "http://13.127.108.14:8099/api/v1/display";
  constructor(private http: HttpClient) { }
  changeURL(url: string) {
    this._url = url;
  }
  getResponses(): Observable<analytics[]> {
    return this.http.get<analytics[]>(this._url).pipe(
      catchError((error: HttpErrorResponse) => {
        return Observable.throw(error.message || "Server Error");
      })
    )
  }
}
