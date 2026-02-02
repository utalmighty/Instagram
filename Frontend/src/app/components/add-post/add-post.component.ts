import { Component, EventEmitter, Output } from '@angular/core';
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
export class AddPostComponent {

  @Output()
  emitter: EventEmitter<boolean> = new EventEmitter();

  httperror!: error;
  fileName!: string;
  postPlaceHolder: string = "Whats on your mind?";
  postConent!: string;
  fileUploadResp!: simpleMessage;
  user!: profile
  post!: post;

  constructor(private instaService: InstagramServiceService, private router: Router) { 
    instaService.loginUser$.subscribe((a)=> this.user = a);
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;
      const formData = new FormData();
      formData.append("file", file);

      this.instaService.postImage(formData)
        .subscribe((r) => {
          this.fileUploadResp = r
          console.log(r);
        },
        (e) => alert("Unable to post."))
    }
  }

  close() {
    this.emitter.emit(false);
  }

  postPost() {
    this.post = new post();
    this.post.postContent = this.postConent;
    this.post.userId = this.user.userId;
    this.post.links = [this.fileUploadResp.message];

    this.instaService.postAPost(this.post).subscribe(
      (resp)=> {
        this.post = resp
        this.router.navigate(['/home']);
      },
      (err)=> this.httperror = err
    );
  }

}