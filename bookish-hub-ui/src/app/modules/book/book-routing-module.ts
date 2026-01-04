import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Main} from './pages/main/main';
import {BookList} from './pages/book-list/book-list';
import {MyBook} from './pages/my-book/my-book';
import {ManageBook} from './pages/manage-book/manage-book';
import {BorrowedBookList} from './pages/borrowed-book-list/borrowed-book-list';
import {ReturnBooks} from './pages/return-books/return-books';
import {authGuard} from '../../services/guard/auth-guard';

const routes: Routes = [
  {
    path: '',
    component: Main,
    canActivate: [authGuard],
    children:[
      {
        path: '',
        component: BookList,
        canActivate: [authGuard],
      },
      {
        path: 'my-book',
        component: MyBook,
        canActivate: [authGuard],
      },
      {
        path: 'manage',
        component: ManageBook,
        canActivate: [authGuard],
      },
      {
        path: 'manage/:bookId',
        component: ManageBook,
        canActivate: [authGuard],
      },
      {
        path: 'borrowed-books',
        component: BorrowedBookList,
        canActivate: [authGuard],
      },
      {
        path: 'my-returned-books',
        component: ReturnBooks,
        canActivate: [authGuard],
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
