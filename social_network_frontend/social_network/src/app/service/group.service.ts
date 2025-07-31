import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { API_ROUTES } from '../api/api.routes';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Group } from '../model/group.model';

@Injectable({ providedIn: 'root' })
export class GroupService {

    constructor(private http: HttpClient, private router: Router) { }

    getAllDorms(): Observable<any[]> {
        return this.http.get<any[]>(API_ROUTES.get_groups);
    }

    addGroup(group: any) {
        return this.http.post(API_ROUTES.add_group, group);
    }

    getGroupById(groupId: number) {
        return this.http.get<Group>(`${API_ROUTES.get_group_by_id}/${groupId}`);
    }

    searchByName(name: string): Observable<any[]> {
        return this.http.get<any[]>(API_ROUTES.get_group_by_name, { params: { name } });
    }

    searchByDescription(desc: string): Observable<any[]> {
        return this.http.get<any[]>(API_ROUTES.get_group_by_desc, { params: { desc } });
    }

    searchByContent(keyword: string): Observable<any[]> {
        return this.http.get<any[]>(API_ROUTES.get_group_elastic, { params: { keyword } });
    }

}