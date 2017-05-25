import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { TimeEntryPopupService } from './time-entry-popup.service';
import { TimeEntryService } from './time-entry.service';

@Component({
    selector: 'jhi-time-entry-delete-dialog',
    templateUrl: './time-entry-delete-dialog.component.html'
})
export class TimeEntryDeleteDialogComponent {

    timeEntry: TimeEntry;

    constructor(
        private timeEntryService: TimeEntryService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timeEntryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timeEntryListModification',
                content: 'Deleted an timeEntry'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-time-entry-delete-popup',
    template: ''
})
export class TimeEntryDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeEntryPopupService: TimeEntryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.timeEntryPopupService
                .open(TimeEntryDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
