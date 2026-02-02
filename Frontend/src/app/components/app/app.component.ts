import { Component } from '@angular/core';
import { ActionsComponent } from '../actions/actions.component';
import { AddPostComponent } from '../add-post/add-post.component';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [ActionsComponent, AddPostComponent, RouterModule, RouterOutlet]
})
export class AppComponent {

  title = 'Instagram';
  postStatus: boolean = false

  togglePost($event: boolean) {
    this.postStatus = !this.postStatus;
  }

  close($event: boolean) {
    this.postStatus = false;
  }
}
