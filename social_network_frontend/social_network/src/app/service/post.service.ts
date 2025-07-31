import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { API_ROUTES } from '../api/api.routes';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Reaction } from '../model/reaction.model';
import { Post } from '../model/post.model';

@Injectable({ providedIn: 'root' })
export class PostService {

    constructor(private http: HttpClient, private router: Router) { }

    getAllPosts(): Observable<any[]> {
        return this.http.get<any[]>(API_ROUTES.get_posts_names);
    }

    addReaction(reaction: Reaction): Observable<Reaction> {
        return this.http.post<Reaction>(API_ROUTES.add_reaction, reaction);
    }

    getPostsByGroupId(groupId: number) {
        return this.http.get<any[]>(`${API_ROUTES.get_posts_by_group}?id=${groupId}`);
    }

    createPost(post: Post): Observable<Post> {
        return this.http.post<Post>(API_ROUTES.add_post, post);
    }

    searchPostsByContent(keyword: string) {
        return this.http.get<Post[]>(API_ROUTES.get_post_by_content, { params: { keyword } });
    }

    searchPostsByFile(keyword: string) {
        return this.http.get<Post[]>(API_ROUTES.get_post_elastic, { params: { keyword } });
    }
}