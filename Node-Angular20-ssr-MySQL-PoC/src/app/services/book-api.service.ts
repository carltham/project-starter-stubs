// src/app/services/user-api.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import BookDTO from '../domain/book';
import { UserDTO } from '../domain/user';

@Injectable({
  providedIn: 'root',
})
export class BookAPIService {
  private baseUrl = '/api/books';
  wasProcessed = true;

  constructor(private http: HttpClient) {}

  getAll(): Observable<BookDTO[]> {
    return this.http.get<UserDTO[]>(this.baseUrl).pipe();
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  create(payload: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, payload);
  }

  update(id: number, payload: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
