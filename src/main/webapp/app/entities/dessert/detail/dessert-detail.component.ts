import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDessert } from '../dessert.model';

@Component({
  selector: 'jhi-dessert-detail',
  templateUrl: './dessert-detail.component.html',
})
export class DessertDetailComponent implements OnInit {
  dessert: IDessert | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dessert }) => {
      this.dessert = dessert;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
