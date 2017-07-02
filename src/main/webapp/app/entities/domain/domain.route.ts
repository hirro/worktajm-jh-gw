import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DomainComponent } from './domain.component';
import { DomainDetailComponent } from './domain-detail.component';
import { DomainPopupComponent } from './domain-dialog.component';
import { DomainDeletePopupComponent } from './domain-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class DomainResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const domainRoute: Routes = [
    {
        path: 'domain',
        component: DomainComponent,
        resolve: {
            'pagingParams': DomainResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmApp.domain.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'domain/:id',
        component: DomainDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmApp.domain.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const domainPopupRoute: Routes = [
    {
        path: 'domain-new',
        component: DomainPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmApp.domain.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'domain/:id/edit',
        component: DomainPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmApp.domain.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'domain/:id/delete',
        component: DomainDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmApp.domain.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
