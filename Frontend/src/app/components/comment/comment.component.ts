import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { comment } from '../../props/comment';
import { error } from '../../props/error';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class CommentComponent implements OnInit {

  httperror!: error;
  username!: string;
  @Input() postId!: string;
  @Input() commentCount!: number;
  comments!: comment[];
  commentPlaceHolder: string = "Comment"
  comment!: string;
  user!: profile;

  @Output()
  emitter: EventEmitter<boolean> = new EventEmitter();

  @Output()
  countEmitter: EventEmitter<number> = new EventEmitter();

  constructor(private service: InstagramServiceService) {
    service.loginUser$.subscribe((r) => {
      this.user = r
      this.username = r.username;
    });
  }

  ngOnInit(): void {
    this.service.getComments(this.postId).subscribe((r) => {
      this.comments = r
      this.comments.forEach((c) => this.service.getUserDetails(c.userId).subscribe((u) => c.profile = u));
    });
  }

  close() {
    this.emitter.emit(false);
  }

  sendComment() {
    if (this.comment && this.comment.trim().length > 0) {
      this.commentCount += 1;
      let newComment: comment = {
        userId: this.user.userId, comment: this.comment, postId: this.postId,
        profile: new profile
      };
      this.service.comment(newComment).subscribe((resp) => resp, (err) => this.httperror = err.error);
      newComment.profile.username = this.username;
      this.comments.push(newComment);
      this.countEmitter.emit(this.commentCount);
      this.comment = ""
    }
    else {
      this.commentPlaceHolder = "Please provide Comment"
    }
  }

}
