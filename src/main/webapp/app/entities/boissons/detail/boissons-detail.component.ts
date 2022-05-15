import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoissons } from '../boissons.model';

@Component({
  selector: 'jhi-boissons-detail',
  templateUrl: './boissons-detail.component.html',
})
export class BoissonsDetailComponent implements OnInit {
  boissons: IBoissons | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boissons }) => {
      this.boissons = boissons;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
