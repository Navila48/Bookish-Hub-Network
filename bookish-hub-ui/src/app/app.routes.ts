import { Routes } from '@angular/router';
import {Login} from './pages/login/login';
import {Register} from './pages/register/register';
import {ActivateAccount} from './pages/activate-account/activate-account';
import {authGuard} from './services/guard/auth-guard';
import {BookList} from './modules/book/pages/book-list/book-list';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'books',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'activate-account',
    component: ActivateAccount
  },
  {
    path: 'books',
    loadChildren: () => import("./modules/book/book-module").then(m => m.BookModule),
    canActivate: [authGuard]
  }
];
