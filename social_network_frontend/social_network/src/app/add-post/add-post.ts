import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../service/post.service';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-add-post',
  standalone: true,
  templateUrl: './add-post.html',
  styleUrls: ['./add-post.css'],
  imports: [CommonModule, FormsModule],
})
export class AddPost {
  content: string = '';
  groupId: number;
  userId: number;

  constructor(private postService: PostService, private route: ActivatedRoute, private router: Router,
    private authService: AuthService) {
    this.groupId = +(this.route.snapshot.paramMap.get('groupid') ?? 0);
    const id = this.authService.getUserId();
    if (id !== null) {
      this.userId = id;
    } else {
      this.userId = 0;
      this.router.navigate(['/home']);
    }
  }

  submitPost() {
    const newPost = {
      id: 0,
      content: this.content,
      creationDate: new Date().toISOString(),
      userId: this.userId,
      groupId: this.groupId,
      reactions: []
    };

    this.postService.createPost(newPost).subscribe({
      next: () => this.router.navigate(['/group', this.groupId]),
      error: (err) => console.error('Greška prilikom kreiranja posta:', err)
    });
  }
}

