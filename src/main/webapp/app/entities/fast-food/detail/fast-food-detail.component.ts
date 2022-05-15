import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFastFood } from '../fast-food.model';

@Component({
  selector: 'jhi-fast-food-detail',
  templateUrl: './fast-food-detail.component.html',
})
export class FastFoodDetailComponent implements OnInit {
  fastFood: IFastFood | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fastFood }) => {
      this.fastFood = fastFood;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
