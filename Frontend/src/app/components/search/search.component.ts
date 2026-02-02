import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { search } from '../../props/search';
import { profile } from '../../props/profile';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  imports: [RouterModule]
})
export class SearchComponent implements OnInit {
  isSearch: boolean = true
  displayText: string = "Followers"
  userId!: string
  searchquery!: string
  previous: any = new Date();
  searchResults: search[] = [];

  constructor(private service: InstagramServiceService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      if (param['type'] == 'follower') {
        this.isSearch = false;
        this.userId = param['name']
        this.getFollowers();
      }
      else if (param['type'] == 'following') {
        this.displayText = "Following"
        this.isSearch = false;
        this.userId = param['name']
        this.getFollowing();
      }
    })
  }

  search(searchword: string) {
    
    if (searchword.trim().length >= 3 && this.timeDifference(2000)) {
      this.searchResults = []
      this.previous = new Date();
      this.searchquery = searchword;
      this.service.getUsersLike(searchword).subscribe((resp) => {
        for (const str of resp) {
          this.service.getUserDetailsByUsername(str).subscribe((r) => {
            this.searchResults.push({ type: 'profile', name: str, profile: r });
          })
        }
      })
      this.service.getHashesLike(searchword).subscribe((resp) => {
        for (const str of resp) {
          this.searchResults.push({ type: 'tag', name: str, profile: new profile });
        }
      })
    }
  }


  getFollowers() {
    this.service.followers(this.userId).subscribe((resp) => {
      for (const element of resp) {
        this.service.getUserDetails(element).subscribe((r) => {
          this.searchResults.push({ type: 'profile', name: r.username, profile: r })
        })
      }
    })
  }

  getFollowing() {
    this.service.followings(this.userId).subscribe((resp) => {
      for (const element of resp) {
        this.service.getUserDetails(element).subscribe((r) => {
          this.searchResults.push({ type: 'profile', name: r.username, profile: r })
        })
      }
    })
  }

  timeDifference(diff: number): boolean {
    let now: any = new Date();
    return now-this.previous >= diff;
  }


}

