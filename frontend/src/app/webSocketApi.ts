import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AppComponent } from './app.component';
import { SearchResultComponent } from './search-result/search-result.component';
import { MedicalDomainSearchComponent } from './medical-domain-search/medical-domain-search.component';
import { Router } from '@angular/router';

export class WebSocketAPI {
    webSocketEndPoint: string = 'http://13.127.108.14:8015/socket';
    topic: string = "/topic/notification";
    stompClient: any;
    appComponent: MedicalDomainSearchComponent;
    constructor(medicalDomainSearchComponent: MedicalDomainSearchComponent){
        this.appComponent = medicalDomainSearchComponent;
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        _this.stompClient.connect({}, function (frame) {
            _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
            });
        }, this.errorCallBack);
    }
    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        _this.stompClient.connect({}, function (frame) {
            _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
                console.log("hsakdhksadj");
                _this.onMessageReceived(sdkEvent);
            });
        }, this.errorCallBack);
        return this.stompClient;
    }

    errorCallBack(error) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

    onMessageReceived(message) {
        console.log("Message Recieved from Server :: " + message);
        console.log("mesaarevcjewd"+message.body);
       // this.appComponent.handleMessage(JSON.parse(message.body));
    }
}