import { NgModule } from "@angular/core";
import {Routes, RouterModule } from "@angular/router";
import {ExpertLoginComponent} from './expert-login/expert-login.component';
import {ExpertRegisterComponent} from './expert-register/expert-register.component';
import { from } from 'rxjs';
import { ExpertDashboardComponent } from './expert-dashboard/expert-dashboard.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import { MovieDomainSearchComponent } from './movie-domain-search/movie-domain-search.component';
import {MedicalDomainSearchComponent} from './medical-domain-search/medical-domain-search.component';
import {SearchResultComponent} from './search-result/search-result.component';
import { ExpertAnalyticsComponent } from './expert-analytics/expert-analytics.component';
import { ExpertAnalyticsMovieComponent } from './expert-analytics-movie/expert-analytics-movie.component';
import { ExpertAnalyticsMedicalComponent } from './expert-analytics-medical/expert-analytics-medical.component';
import { ExpertAnalyticsGraphComponent } from './expert-analytics-graph/expert-analytics-graph.component';
import { ExpertValidateDashboardComponent } from './expert-validate-dashboard/expert-validate-dashboard.component';


const routes: Routes = [
  {path: '', redirectTo:'dashboard',pathMatch : 'full'},
  {path: 'expert-analytics', component: ExpertAnalyticsComponent},
  {path: 'expert-analytics/medical', component: ExpertAnalyticsMedicalComponent},
  {path: 'expert-analytics/movie', component: ExpertAnalyticsMovieComponent},
  {path: 'expert-analytics/graph', component: ExpertAnalyticsGraphComponent},
  {path: 'medical-domain', component: MedicalDomainSearchComponent},
  {path: 'movie-domain', component: MovieDomainSearchComponent},
  {path: 'login', component: ExpertLoginComponent},
  {path: 'register', component: ExpertRegisterComponent},
  {path: 'expert-dashboard', component: ExpertDashboardComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'search-result', component: SearchResultComponent},
  {path:'expert-validate',component:ExpertValidateDashboardComponent},

  {path: '**', component:DashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {}
export const routingComponents = [MedicalDomainSearchComponent,SearchResultComponent, MovieDomainSearchComponent,ExpertLoginComponent, ExpertRegisterComponent, ExpertDashboardComponent, DashboardComponent, ExpertAnalyticsComponent, ExpertAnalyticsMedicalComponent, ExpertAnalyticsMovieComponent, ExpertAnalyticsGraphComponent,ExpertValidateDashboardComponent];
