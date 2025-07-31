import { Component } from '@angular/core';
import { PostService } from '../service/post.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

interface PostDisplay {
  content: string;
  creationDate: string;
  userName?: string;
  userId?: number;
  groupId: number;
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-search-posts',
  templateUrl: './search-posts.html',
  styleUrls: ['./search-posts.css']
})
export class SearchPosts {


  searchType: 'content' | 'file' = 'content';
  keyword: string = '';
  posts: PostDisplay[] = [];
  searched: boolean = false;

  constructor(private postService: PostService, private router:Router) { }

  onSearch() {
    this.posts = [];
    this.searched = false;

    if (!this.keyword.trim()) {
      return;
    }

    if (this.searchType === 'content') {
      this.postService.searchPostsByContent(this.keyword).subscribe(posts => {
        this.posts = posts;
        this.searched = true;
      });
    } else if (this.searchType === 'file') {
      this.postService.searchPostsByFile(this.keyword).subscribe(posts => {
        this.posts = posts;
        this.searched = true;
      });
    }
  }

  goBack() {
    this.router.navigate(['/home']);
  }
}
