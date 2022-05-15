import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'nadiaDeliveryFoodApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'responsable-restaurant',
        data: { pageTitle: 'nadiaDeliveryFoodApp.responsableRestaurant.home.title' },
        loadChildren: () => import('./responsable-restaurant/responsable-restaurant.module').then(m => m.ResponsableRestaurantModule),
      },
      {
        path: 'restaurant',
        data: { pageTitle: 'nadiaDeliveryFoodApp.restaurant.home.title' },
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.RestaurantModule),
      },
      {
        path: 'livreur',
        data: { pageTitle: 'nadiaDeliveryFoodApp.livreur.home.title' },
        loadChildren: () => import('./livreur/livreur.module').then(m => m.LivreurModule),
      },
      {
        path: 'menu',
        data: { pageTitle: 'nadiaDeliveryFoodApp.menu.home.title' },
        loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule),
      },
      {
        path: 'plat',
        data: { pageTitle: 'nadiaDeliveryFoodApp.plat.home.title' },
        loadChildren: () => import('./plat/plat.module').then(m => m.PlatModule),
      },
      {
        path: 'fast-food',
        data: { pageTitle: 'nadiaDeliveryFoodApp.fastFood.home.title' },
        loadChildren: () => import('./fast-food/fast-food.module').then(m => m.FastFoodModule),
      },
      {
        path: 'boissons',
        data: { pageTitle: 'nadiaDeliveryFoodApp.boissons.home.title' },
        loadChildren: () => import('./boissons/boissons.module').then(m => m.BoissonsModule),
      },
      {
        path: 'dessert',
        data: { pageTitle: 'nadiaDeliveryFoodApp.dessert.home.title' },
        loadChildren: () => import('./dessert/dessert.module').then(m => m.DessertModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'nadiaDeliveryFoodApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'commande-details',
        data: { pageTitle: 'nadiaDeliveryFoodApp.commandeDetails.home.title' },
        loadChildren: () => import('./commande-details/commande-details.module').then(m => m.CommandeDetailsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
