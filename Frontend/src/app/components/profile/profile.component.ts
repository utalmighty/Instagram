import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { profile } from '../../props/profile';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { PostComponent } from '../post/post.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  imports: [RouterModule, PostComponent]
})
export class ProfileComponent implements OnInit {
  isProfile: boolean = false
  user!: profile
  chosenUser!: profile
  following: boolean = false
  sharedname: string = ""
  feedType: string = "profile"
  isLoggedInUser:boolean = false

  constructor(private route: ActivatedRoute, private service: InstagramServiceService) {
    service.loginUser$.subscribe((a) => this.user = a);
  }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      if (param['type'] == "username") {
        this.isProfile = true;
        if (!param['name']) {
          this.chosenUser = this.user
          this.sharedname = this.chosenUser.userId
          this.isLoggedInUser = true
          console.log(this.chosenUser.verified);
        }
        else {
          this.service.getUserDetailsByUsername(param['name']).subscribe(
            (resp) => {
              this.chosenUser = resp
              console.log(this.chosenUser.verified);
              this.sharedname = this.chosenUser.userId
              this.service.isFollowing(this.user.userId, this.chosenUser.userId). subscribe(
                (res)=> this.following = res.message.toString() == "true"
              )}
            // (err)=> TODO: 404 error
          );
        }
        
      }
      else if (param['type'] == "tag") {
        this.feedType = "tag"
        this.sharedname = param['name']
      }
      else {
        this.feedType = "trending"
        this.sharedname = 'trending'
      }
    })
     
  }

  follow() {
    this.service.follow(this.user.userId, this.chosenUser.userId).subscribe((resp)=> {
      if (this.following)
        this.chosenUser.followersCount -= 1
      else 
        this.chosenUser.followersCount += 1
      this.following = !this.following
    });
  }


  logout() {
    location.reload();
  }
}
