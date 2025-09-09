import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Main} from './pages/main/main';

const routes: Routes = [
  {
    path: '',
    component: Main
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
