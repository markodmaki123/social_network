import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { API_ROUTES } from '../api/api.routes';
import { UserDTO } from '../model/user.model';
import { BehaviorSubject, tap } from 'rxjs';


@Injectable({ providedIn: 'root' })
export class AuthService {
  user: UserDTO | null = null;

  constructor(private http: HttpClient, private router: Router) {
    this.loadUser();
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }


  login(username: string, password: string) {
    return this.http.post<UserDTO>(API_ROUTES.login, { username, password }).pipe(
      tap(user => {
        this.setUser(user);
      })
    );
  }

  getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  getUserId(): number | null {
    const userString = localStorage.getItem('user');
    if (userString) {
      try {
        const user = JSON.parse(userString);
        return user.id ?? null;
      } catch (e) {
        console.error('Greška pri parsiranju user objekta iz localStorage:', e);
        return null;
      }
    }
    return null;
  }

  isLoggedIn(): boolean {
    if (this.isBrowser()) {
      return !!this.getUser();
    }
    else return false;
  }


  private loadUser() {
    if (this.isBrowser()) {
      const stored = localStorage.getItem('user');
      if (stored) {
        this.user = JSON.parse(stored);
      }
    }
  }

  setUser(user: UserDTO) {
    this.user = user;
    if (this.isBrowser()) {
      localStorage.setItem('user', JSON.stringify(user));
    }
  }

  logout() {
    this.user = null;
    if (this.isBrowser()) {
      localStorage.removeItem('user');
    }
  }
}