import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'likepipe'
})
export class LikePipe implements PipeTransform {

  transform(value: boolean): string {
    
    if (value) return "Unlike";
    return "Like";
  }

}
