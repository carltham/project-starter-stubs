import { Routes } from "@angular/router";
import { HomeComponent } from "./home.component/home.component";
import { UsersComponent } from "./users.component/users.component";

export const routes: Routes = [
  /*{
    path: '',
    loadComponent: () => import('./home.component/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'users',
    loadComponent: () => import('./users.component/users.component').then((m) => m.UsersComponent),
  },*/
  {
    path: "",
    title: "Homepage",
    component: HomeComponent,
  },
  {
    path: "users",
    title: "Homepage",
    component: UsersComponent,
  },
  { path: "**", redirectTo: "" },
];
