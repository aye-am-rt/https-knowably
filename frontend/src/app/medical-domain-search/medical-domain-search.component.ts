import { Component, OnInit } from '@angular/core';
import {SpeechService} from '../speech-service/speech.service';
import {MedicalSearchService} from '../medical_search_service/medical-search.service';
import { Router } from '@angular/router';
import {WebSocketService} from '../websocket-service/websocket.service';
import { from } from 'rxjs';


@Component({
  selector: 'app-medical-domain-search',
  templateUrl: './medical-domain-search.component.html',
  styleUrls: ['./medical-domain-search.component.css']
})
export class MedicalDomainSearchComponent implements OnInit {

  //SpeechRecognition variables
    startListenButton: boolean;
    stopListeningButton: boolean;
    speechData: string;

    notifications:string[];
    results:String;
    name: string;
   constructor(private speechService: SpeechService,private webSocketService: WebSocketService,  private medicalSearchService: MedicalSearchService, private route:Router ) {
     this.startListenButton = true;
     this.stopListeningButton = false;
     this.speechData = "";
   }
   formValue:String;
   ngOnInit() {
     localStorage.clear();
     let stompClient =this.webSocketService.connect();
                               stompClient.connect({},frame =>{
                                 stompClient.subscribe('/topic/notification',notifications=>{
                                   this.notifications=JSON.parse(notifications.body);
                                   localStorage.setItem('result', this.notifications.join(':'));
                                   console.log(this.notifications);
                                   this.results="results";
                                   this.route.navigateByUrl('/search-result');
                                 })
                               });
   }

   ngOnDestroy() {
     this.speechService.DestroySpeechObject();
   }

   userSearch(searchQuery){
     console.log(searchQuery);
     localStorage.setItem('domain', "medical");
      this.medicalSearchService.userSearchService(searchQuery)
                         .subscribe(data=>{
                               console.log(data);
                               

                              },error=>{
                                console.log(error);
                                this.route.navigateByUrl('/medical-domain');
                              });


    }

   activateSpeechSearch(): void {
     console.log("listening");
     this.startListenButton = false;

     this.speechService.record()
         .subscribe(
         //listener
         (value) => {
             this.speechData = value;
             this.formValue = value;
             console.log('listener.speechData:', value);
         },
         //error
         (err) => {
             console.log(err);
             if (err.error == "no-speech") {
                 console.log("--restarting service--");
                 this.activateSpeechSearch();
             }
         },
         //completion
         () => {
             this.startListenButton = true;
             console.log("--complete--");
             this.sendMessageFromSpeechRecognition();
             console.log('this.stopListeningButton', this.stopListeningButton);
             // if (!this.stopListeningButton) {
             //   this.activateSpeechSearch();
             // }

         });
   }

   deActivateSpeechSearch(): void {
     console.log("stop listening")
     this.startListenButton = true;
     this.stopListeningButton = true;
     this.speechService.DestroySpeechObject();
   }

   sendMessageFromSpeechRecognition(): void {
     this.speechService.DestroySpeechObject();
     console.log("fjds"+this.formValue);
     //this.sendMessage();
     // setTimeout(() => {
     //   console.log('clicking');
     //   this.sendMessage();
     // }, 8000);
     // let element: HTMLElement = this.sendButtonRef.nativeElement as HTMLElement;
     // element.click();
   }

}
