import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorktajmGwSharedModule } from '../../shared';
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
} from './';

const ENTITY_STATES = [
    ...timeEntryRoute,
    ...timeEntryPopupRoute,
];

@NgModule({
    imports: [
        WorktajmGwSharedModule,
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
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorktajmGwTimeEntryModule {}
