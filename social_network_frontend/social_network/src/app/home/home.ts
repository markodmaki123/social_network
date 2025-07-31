import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { API_ROUTES } from '../api/api.routes';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class Home implements OnInit {
  email = '';
  password = '';
  loginFailed = false;
  groups: any[] = [];
  isLoggedIn = false;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

    if (this.isLoggedIn) {
      this.http.get(API_ROUTES.get_groups).subscribe(data => {
        this.groups = data as any[];
      });
    }
  }

  get user() {
    return this.authService.user;
  }

  login() {
    this.authService.login(this.email, this.password).subscribe({
      next: (user) => {
        this.authService.setUser(user);
        this.loginFailed = false;
        this.isLoggedIn = true;

        this.http.get(API_ROUTES.get_groups).subscribe(data => {
          this.groups = data as any[];
        });
      },
      error: () => {
        this.loginFailed = true;
      }
    });
  }

  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.groups = [];
    this.email = '';
    this.password = '';
  }

  goToGroup(groupId: number) {
    this.router.navigate(['/group', groupId]);
  }

  addGroup() {
    this.router.navigate(['/add-group']);
  }

  searchPosts() {
    this.router.navigate(['/post/search']);
  }

  searchGroups() {
    this.router.navigate(['/groups/search']);
  }
}
