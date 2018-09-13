import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'mapIterator'  
})
export class MapIteratorPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    let result = [];
    console.log('map pipe invoked with value',value);
    if(value && value.entries) {
      for (var [key, value] of value.entries()) {
        result.push({ key, value });
      }
    } else {
      for(let key in value) {
        result.push({ key, value: value[key] });
      }
    }

    return result;  
  }

}
