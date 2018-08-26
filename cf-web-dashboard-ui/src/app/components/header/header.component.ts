import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public user: string;
  public localeDate: string;
  public localeTime: string;

  constructor() { }

  ngOnInit() {

    this.user = 'Barath';
    setInterval(() => {
      const date = new Date();
      this.localeDate = date.toLocaleDateString();
      this.localeTime = date.toLocaleTimeString();
    }, 1);
  }

}
