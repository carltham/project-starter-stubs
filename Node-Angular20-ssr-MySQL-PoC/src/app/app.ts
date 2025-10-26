import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Route, RouterLink, RouterOutlet } from '@angular/router';
import { routeListeners } from './app.routes';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  routes: any[] = [];
  protected readonly title = signal('angular20-ssr');
  constructor() {
    routeListeners.forEach((route: Route) => {
      if (route['path'] !== '**') {
        this.routes.push({ path: route['path'], title: route['title'] });
      }
    });
  }
}
