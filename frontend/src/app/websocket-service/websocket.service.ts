import {Injectable} from "@angular/core";

import * as Stomp from 'stompjs';
import * as SockJs from 'sockjs-client';
import {Subject} from "rxjs";
import { MedicalDomainSearchComponent } from '../medical-domain-search/medical-domain-search.component';
// import {Observable, Subject} from "rxjs";
// var SockJs = require("sockjs-client");
// var Stomp = require("stompjs");


@Injectable()
export class WebSocketService {

  // private stompClient: any;
  // private socketUri = "http://localhost:8080/socket";
  // private senderEndPoint = "/app/notification";
  // private recieverEndPoint = "/topic/notification";
  public notifications:String;
  public results:String;

  private messageSubject = new Subject<any>();

  constructor() {
  let socket = new SockJs(`https://knowably.stackroute.io:8080/notification-service/socket`);

    let stompClient = Stomp.over(socket);
  }
    private result:string;
    connect() {
    let socket = new SockJs(`https://knowably.stackroute.io:8443/socket`);

        let stompClient = Stomp.over(socket);

        return stompClient;
    }

    // storeResult(queryResults){
    //   this.results =queryResults;
    // }
    // getResult(){
    //   return this.results;
    // }
  // connect() {
  //   console.log("connect");
  //   const socket = new SockJs(this.socketUri);
  //   this.stompClient = Stomp.over(socket);
  //   return this.stompClient;
  //
  // }

  /**
   * Disconnects websocket service
   */
/*  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }*/

  /**
   * Sends message to websocket server
   * @param message JSON object to be sent to websocket
   */
/*  sendNotification(notification: {}) {
    this.stompClient.send(this.senderEndPoint, {}, notification);
  }

  public getMessage(): Observable<any> {
    return this.messageSubject.asObservable();
  }*/

}
