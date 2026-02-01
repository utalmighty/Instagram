import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {

  transform(date: string): string {
    return this.timeAgo(date);
  }

  timeAgo(date: string): string {
    let datePast: Date = new Date(Date.parse(date));
    const seconds = Math.floor((new Date().getTime() - datePast.getTime()) / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(days / 365);
  
    if (seconds < 60) {
      return 'just now';
    } else if (minutes < 60) {
      return minutes + ' minutes ago';
    } else if (hours < 24) {
      return hours + ' hours ago';
    } else if (days < 30) {
      return days + ' days ago';
    } else if (months < 12) {
      return months + ' months ago';
    } else {
      return years + ' years ago';
    }
  }

}
