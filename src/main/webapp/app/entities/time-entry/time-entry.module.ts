import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorktajmSharedModule } from '../../shared';
import { WorktajmAdminModule } from '../../admin/admin.module';
import {
    TimeEntryService,
    TimeEntryPopupService,
    TimeEntryComponent,
    TimeEntryDetailComponent,
    TimeEntryDialogComponent,
    TimeEntryPopupComponent,
    TimeEntryDeletePopupComponent,
    TimeEntryDeleteDialogComponent,
    timeEntryRoute,
    timeEntryPopupRoute,
    TimeEntryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...timeEntryRoute,
    ...timeEntryPopupRoute,
];

@NgModule({
    imports: [
        WorktajmSharedModule,
        WorktajmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TimeEntryComponent,
        TimeEntryDetailComponent,
        TimeEntryDialogComponent,
        TimeEntryDeleteDialogComponent,
        TimeEntryPopupComponent,
        TimeEntryDeletePopupComponent,
    ],
    entryComponents: [
        TimeEntryComponent,
        TimeEntryDialogComponent,
        TimeEntryPopupComponent,
        TimeEntryDeleteDialogComponent,
        TimeEntryDeletePopupComponent,
    ],
    providers: [
        TimeEntryService,
        TimeEntryPopupService,
        TimeEntryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorktajmTimeEntryModule {}
