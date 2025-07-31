import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { Post } from '../model/post.model';
import { Reaction } from '../model/reaction.model';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-posts',
  imports: [CommonModule],
  templateUrl: './post.html',
  styleUrls: ['./post.css']
})
export class Posts implements OnInit {
  posts: Post[] = [];
  currentUserId = Number(localStorage.getItem('userId'));

  constructor(private postService: PostService, private route: ActivatedRoute) { }

  ngOnInit() {
    const groupId = this.route.snapshot.paramMap.get('groupId');
    if (groupId) {
      this.postService.getPostsByGroupId(+groupId).subscribe(posts => {
        this.posts = posts;
      });
    }
  }

  loadPosts() {
    const groupId = this.route.snapshot.paramMap.get('groupId');
    if (groupId) {
      this.postService.getPostsByGroupId(+groupId).subscribe(posts => {
        this.posts = posts;
      });
    }
  }

  getScore(post: Post): number {
    let score = 0;
    post.reactions.forEach(r => {
      if (r.reactionType === 'LIKE' || r.reactionType === 'HEART') score += 1;
      if (r.reactionType === 'DISLIKE') score -= 1;
    });
    return score;
  }

  react(postId: number, type: 'LIKE' | 'DISLIKE' | 'HEART') {
    const reaction: Reaction = {
      id: 0,
      createdAt: '',
      userId: this.currentUserId,
      reactionType: type,
      postId: postId
    };
    this.postService.addReaction(reaction).subscribe(() => this.loadPosts());
  }
}
