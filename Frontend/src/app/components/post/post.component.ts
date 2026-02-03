import { Component, Input, OnInit } from '@angular/core';
import { post } from '../../props/post'
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { RouterModule } from '@angular/router';
import { CommentComponent } from '../comment/comment.component';
import { LikePipe } from '../../pipes/like.pipe';
import { TimeAgoPipe } from '../../pipes/time-ago.pipe';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  imports: [RouterModule, CommentComponent, LikePipe, TimeAgoPipe]
})
export class PostComponent implements OnInit {

  comment!: boolean;
  commentNumber!: number;
  postId!: string;
  postIds!: string[];
  posts!: post[];
  selectedPost!: post;
  loggedInUser!: profile;
  @Input() feedType: string = 'userfeed'
  @Input() searchQuery!: string;

  constructor(private service: InstagramServiceService) {}
  
  ngOnInit(): void {
    this.service.loginUser$.subscribe(user => this.loggedInUser = user);
    if (this.feedType == 'userfeed') {
      this.fetchUserFeed()
    } else if (this.feedType == 'profile') {
      this.fetchUserPost()
    } else if (this.feedType == 'tag') {      
      this.fetchByTag()
    } else if (this.feedType == 'trending') {
      this.fetchTrending()
    }
  }

  fetchTrending(){
    this.postIds = []
    this.service.getTrendings().subscribe((r) => {
      this.postIds = r      
      this.service.getMultiplePost(this.postIds).subscribe((resp) => {
        this.posts = resp
        this.posts.forEach(
          (p) => {
            this.service.getUserDetails(p.userId).subscribe((pro) => p.profile = pro)
            this.service.likestatus(this.loggedInUser.userId, p.id).subscribe((pro) => p.isLiked = this.booleanStringToBoolean(pro.message))
          });
      })
    });
  }

  fetchByTag() {
    this.postIds = []
    this.service.getPostsByTag(this.searchQuery).subscribe((r) => {
      this.postIds = r
      this.postIds.reverse()
      this.service.getMultiplePost(this.postIds).subscribe((resp) => {
        this.posts = resp
        this.posts.forEach(
          (p) => {
            this.service.getUserDetails(p.userId).subscribe((pro) => p.profile = pro)
            this.service.likestatus(this.loggedInUser.userId, p.id).subscribe((pro) => p.isLiked = this.booleanStringToBoolean(pro.message))
          });
      })
    });
  }

  fetchUserFeed() {
    this.postIds = []    
    this.service.getFeed(this.loggedInUser.userId).subscribe((r) => {
      this.postIds = r
      this.postIds.reverse()
      // this.postIds = ["65e61463f866705078abbb66", "65e61837f866705078abbb67", "65e6bc9dbc15b2351cf55809"]
      this.service.getMultiplePost(this.postIds).subscribe((resp) => {
        this.posts = resp
        this.posts.forEach(
          (p) => {
            this.service.getUserDetails(p.userId).subscribe((pro) => p.profile = pro)
            this.service.likestatus(this.loggedInUser.userId, p.id).subscribe((pro) => p.isLiked = this.booleanStringToBoolean(pro.message))
          });
      })
    });
  }

  fetchUserPost() {
    this.postIds = []
    this.service.getPostsOfUser(this.searchQuery).subscribe((r) => {
      this.posts = r
      this.posts.reverse()
      this.posts.forEach(
        (p) => {
          this.service.getUserDetails(p.userId).subscribe((pro) => p.profile = pro)
          this.service.likestatus(this.loggedInUser.userId, p.id).subscribe((pro) => p.isLiked = this.booleanStringToBoolean(pro.message))
        });
    });
  }

  commentPopup(post: post): void {
    this.comment = true
    this.postId = post.id
    this.commentNumber = post.commentCount
    this.selectedPost = post
  }

  eventFromChild($event: boolean) {
    this.comment = false
  }

  commentEvent($event: number) {
    this.selectedPost.commentCount = $event;
  }

  booleanStringToBoolean(booleanStr: String): boolean {
    return booleanStr.toString() == "true";
  }

  like(post: post) {
    post.isLiked = !post.isLiked;
    if (post.isLiked) post.likeCount += 1;
    else post.likeCount -= 1;
    this.service.like(this.loggedInUser.userId, post.id).subscribe(resp => resp);
  }
}
