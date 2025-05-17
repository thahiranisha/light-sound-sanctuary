import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import {SantuaryMapComponent} from "./santuary-map/santuary-map.component";

const routes: Routes = [
  { path: '', component: LandingComponent }, // Your landing page here
  { path: 'beacons', component: SantuaryMapComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
