import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Main} from './pages/main/main';
import {BookList} from './pages/book-list/book-list';
import {MyBook} from './pages/my-book/my-book';
import {ManageBook} from './pages/manage-book/manage-book';
import {BorrowedBookList} from './pages/borrowed-book-list/borrowed-book-list';

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
      },
      {
        path: 'manage',
        component: ManageBook
      },
      {
        path: 'manage/:bookId',
        component: ManageBook
      },
      {
        path: 'borrowed-books',
        component: BorrowedBookList
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
