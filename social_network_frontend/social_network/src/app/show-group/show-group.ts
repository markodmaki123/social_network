import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../service/post.service';
import { GroupService } from '../service/group.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Reaction } from '../model/reaction.model';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-show-group',
  standalone: true,
  templateUrl: './show-group.html',
  imports: [CommonModule],
  styleUrls: ['./show-group.css']
})
export class ShowGroup implements OnInit {
  groupId!: number;
  group: any;
  posts: any[] = [];
  avgLikes: number = 0;

  constructor(
    private route: ActivatedRoute,
    private groupService: GroupService,
    private postService: PostService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.groupId = +params.get('groupid')!;

      this.groupService.getGroupById(this.groupId).subscribe(group => {
        this.group = group;
      });

      this.postService.getPostsByGroupId(this.groupId).subscribe(posts => {
        this.posts = posts;
        this.calculateAvgLikes();
      });
    });
  }

  goToAddPost() {
    this.router.navigate(['/group', this.groupId, 'add-post']);
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  calculateAvgLikes() {
    if (!this.posts.length) {
      this.avgLikes = 0;
      return;
    }

    let totalLikes = 0;
    for (const post of this.posts) {
      totalLikes += post.reactions?.filter((r: any) => r.reactionType === 'LIKE' || r.reactionType === 'HEART').length || 0;
    }
    this.avgLikes = totalLikes / this.posts.length;
  }

  react(postId: number, type: 'LIKE' | 'DISLIKE' | 'HEART') {
    const userId = this.authService.getUserId();
    if (!userId) {
      alert('Morate biti ulogovani da biste reagovali.');
      this.router.navigate(['/home']);  
      return;
    }

    const reaction: Reaction = {
      id: 0, 
      createdAt: new Date().toISOString(),
      userId: userId,
      reactionType: type,
      postId: postId
    };

    this.postService.addReaction(reaction).subscribe({
      next: () => {
        this.postService.getPostsByGroupId(this.groupId).subscribe(posts => {
          this.posts = posts;
          this.calculateAvgLikes();
        });
      },
      error: () => alert('Greška pri dodavanju reakcije')
    });
  }
  
}
