import { Routes } from '@angular/router';
import { PostComponent } from './components/post/post.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { SearchComponent } from './components/search/search.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AddPostComponent } from './components/add-post/add-post.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: "home", component: PostComponent, canActivate: [AuthGuard] },
  { path: "login", component: LoginComponent },
  { path: "register", component: RegisterComponent },
  { path: "search/:type/:name", component: SearchComponent, canActivate: [AuthGuard] },
  { path: "profile/:type/:name", component: ProfileComponent, canActivate: [AuthGuard] },
  { path: "create", component: AddPostComponent, canActivate: [AuthGuard] },
  { path: "", component: LoginComponent, pathMatch: 'full' },
  { path: "**", component: LoginComponent, pathMatch: 'full' }
];