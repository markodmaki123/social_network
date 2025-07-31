import { Component } from '@angular/core';
import { GroupService } from '../service/group.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule,FormsModule],
  selector: 'app-search-groups',
  templateUrl: './search-groups.html',
  styleUrls: ['./search-groups.css']
})
export class SearchGroups {
  searchType: 'name' | 'description' | 'content' = 'name'; 
  keyword: string = '';
  groups: any[] = [];
  loading: boolean = false;
  error: string | null = null;

  constructor(private groupService: GroupService, private router: Router) {}

  search() {
    this.error = null;
    if (!this.keyword.trim()) {
      this.groups = [];
      return;
    }

    this.loading = true;
    let obs;

    if (this.searchType === 'name') {
      obs = this.groupService.searchByName(this.keyword);
    } else if (this.searchType === 'description') {
      obs = this.groupService.searchByDescription(this.keyword);
    } else { 
      obs = this.groupService.searchByContent(this.keyword);
    }

    obs.subscribe({
      next: data => {
        this.groups = data;
        this.loading = false;
      },
      error: err => {
        this.error = 'Error searching groups';
        this.loading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/home']);
  }
}

