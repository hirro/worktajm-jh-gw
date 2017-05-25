import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { TimeEntry } from './time-entry.model';
import { TimeEntryService } from './time-entry.service';
@Injectable()
export class TimeEntryPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private timeEntryService: TimeEntryService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.timeEntryService.find(id).subscribe((timeEntry) => {
                timeEntry.start = this.datePipe
                    .transform(timeEntry.start, 'yyyy-MM-ddThh:mm');
                timeEntry.end = this.datePipe
                    .transform(timeEntry.end, 'yyyy-MM-ddThh:mm');
                this.timeEntryModalRef(component, timeEntry);
            });
        } else {
            return this.timeEntryModalRef(component, new TimeEntry());
        }
    }

    timeEntryModalRef(component: Component, timeEntry: TimeEntry): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.timeEntry = timeEntry;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
