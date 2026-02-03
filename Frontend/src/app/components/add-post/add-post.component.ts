import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { error } from '../../props/error';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { post } from '../../props/post';
import { profile } from '../../props/profile';
import { simpleMessage } from '../../props/simpleMessage';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css'],
  imports: [FormsModule]
})
export class AddPostComponent implements OnInit, OnDestroy {

  httperror!: error;
  fileName!: string;
  postPlaceHolder: string = "Whats on your mind?";
  postConent!: string;
  fileUploadResp!: simpleMessage;
  loggedInUser!: profile
  post!: post;
  @Output()
  emitter: EventEmitter<boolean> = new EventEmitter();

  constructor(private service: InstagramServiceService, private router: Router) { }
  
  ngOnInit(): void {
    this.service.loginUser$.subscribe(user => this.loggedInUser = user);
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    
    if (file) {
      this.fileName = file.name;
      const formData = new FormData();
      formData.append("file", file);
      
      this.service.postImage(formData).subscribe({
        next: (resp) => {
          this.fileUploadResp = resp
          console.log(resp);
        },
        error: (err) => alert("Unable to post.")
      })
    }
  }
  
  close() {
    this.emitter.emit(false);
  }
  
  postPost() {
    this.post = new post();
    this.post.postContent = this.postConent;
    this.post.userId = this.loggedInUser.userId;
    this.post.links = [this.fileUploadResp.message];
    
    this.service.postAPost(this.post).subscribe({
      next: (resp)=> {
        this.post = resp
        this.router.navigate(['/home']);
      },
      error: (err)=> this.httperror = err
    });
  }

  ngOnDestroy(): void {
    if (this.service.loginUser$) {
      this.service.loginUser$.unsubscribe();
    }
  }
}