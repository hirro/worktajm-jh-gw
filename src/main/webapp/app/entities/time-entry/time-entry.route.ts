import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TimeEntryComponent } from './time-entry.component';
import { TimeEntryDetailComponent } from './time-entry-detail.component';
import { TimeEntryPopupComponent } from './time-entry-dialog.component';
import { TimeEntryDeletePopupComponent } from './time-entry-delete-dialog.component';

import { Principal } from '../../shared';

export const timeEntryRoute: Routes = [
    {
        path: 'time-entry',
        component: TimeEntryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmGwApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'time-entry/:id',
        component: TimeEntryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmGwApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timeEntryPopupRoute: Routes = [
    {
        path: 'time-entry-new',
        component: TimeEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmGwApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-entry/:id/edit',
        component: TimeEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmGwApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-entry/:id/delete',
        component: TimeEntryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'worktajmGwApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];