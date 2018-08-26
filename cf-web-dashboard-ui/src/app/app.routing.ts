import {Routes, Router, RouterModule} from '@angular/router';


const routes: Routes = [
    {path: '', redirectTo: 'dashboard', pathMatch: 'full'}
  
];

export const AppRoutingModule = RouterModule.forRoot(routes,{ useHash: false,enableTracing: true });