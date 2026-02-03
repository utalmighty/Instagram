import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { RouterModule } from '@angular/router';
import { profile } from '../../props/profile';

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.css'],
  imports: [RouterModule]
})
export class ActionsComponent implements OnInit {
  
  loggedInUser!: profile;
  @Output()
  emitter: EventEmitter<boolean> = new EventEmitter();

  constructor(private service: InstagramServiceService) {}

  ngOnInit(): void {
    this.service.loginUser$.subscribe(user => this.loggedInUser = user);
  }
  
  openPost() {
    this.emitter.emit(true);
  }
}
