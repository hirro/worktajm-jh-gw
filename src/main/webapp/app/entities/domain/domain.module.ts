import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorktajmSharedModule } from '../../shared';
import { WorktajmAdminModule } from '../../admin/admin.module';
import {
    DomainService,
    DomainPopupService,
    DomainComponent,
    DomainDetailComponent,
    DomainDialogComponent,
    DomainPopupComponent,
    DomainDeletePopupComponent,
    DomainDeleteDialogComponent,
    domainRoute,
    domainPopupRoute,
    DomainResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...domainRoute,
    ...domainPopupRoute,
];

@NgModule({
    imports: [
        WorktajmSharedModule,
        WorktajmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DomainComponent,
        DomainDetailComponent,
        DomainDialogComponent,
        DomainDeleteDialogComponent,
        DomainPopupComponent,
        DomainDeletePopupComponent,
    ],
    entryComponents: [
        DomainComponent,
        DomainDialogComponent,
        DomainPopupComponent,
        DomainDeleteDialogComponent,
        DomainDeletePopupComponent,
    ],
    providers: [
        DomainService,
        DomainPopupService,
        DomainResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorktajmDomainModule {}
