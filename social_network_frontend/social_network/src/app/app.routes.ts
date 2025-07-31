import { Routes } from '@angular/router';
import { Home } from './home/home';
import { AddGroup } from './add-group/add-group';
import { AddPost } from './add-post/add-post';
import { ShowGroup } from './show-group/show-group';
import { SearchPosts } from './search-posts/search-posts';
import { SearchGroups } from './search-groups/search-groups';

export const routes: Routes = [{
        path: 'home', component: Home
    },
    {
        path: 'add-group', component: AddGroup
    },
    {
        path: 'group/:groupid/add-post', component: AddPost
    },
    {
        path: 'group/:groupid', component: ShowGroup
    },
    {
        path: 'group/:groupid/post/:postid', component: ShowGroup
    },
    {
        path: 'post/search', component: SearchPosts
    },
    {
        path: 'groups/search', component: SearchGroups
    },
];
