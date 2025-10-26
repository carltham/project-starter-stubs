import { Routes } from '@angular/router';
import { BooksComponent } from './components/books.component/books.component';
import { HomeComponent } from './components/home.component/home.component';
import { UsersComponent } from './components/users.component/users.component';

export const routeListeners: Routes = [
  {
    path: '',
    title: 'Home',
    component: HomeComponent,
  },
  {
    path: 'users',
    title: 'Users',
    component: UsersComponent,
  },
  {
    path: 'books',
    title: 'Books',
    component: BooksComponent,
  },
  { path: '**', redirectTo: '' },
];
