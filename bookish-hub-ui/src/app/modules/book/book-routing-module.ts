import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Main} from './pages/main/main';
import {BookList} from './pages/book-list/book-list';
import {MyBook} from './pages/my-book/my-book';

const routes: Routes = [
  {
    path: '',
    component: Main,
    children:[
      {
        path: '',
        component: BookList
      },
      {
        path: 'my-book',
        component: MyBook
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
