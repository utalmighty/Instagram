import { Component, EventEmitter, Output } from '@angular/core';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.css'],
  imports: [RouterModule]
})
export class ActionsComponent {
  userId!: string;
  ppId!: string;
  @Output()
  emitter: EventEmitter<boolean> = new EventEmitter();

  constructor(private service: InstagramServiceService) {
    this.service.loginUser$.subscribe((r) => {
      this.userId = r.userId
      this.ppId = r.ppId
    })
  }

  openPost() {
    this.emitter.emit(true);
  }

}
