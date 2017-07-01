import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { TimeEntryService } from './time-entry.service';

@Component({
    selector: 'jhi-time-entry-detail',
    templateUrl: './time-entry-detail.component.html'
})
export class TimeEntryDetailComponent implements OnInit, OnDestroy {

    timeEntry: TimeEntry;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private timeEntryService: TimeEntryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTimeEntries();
    }

    load(id) {
        this.timeEntryService.find(id).subscribe((timeEntry) => {
            this.timeEntry = timeEntry;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimeEntries() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timeEntryListModification',
            (response) => this.load(this.timeEntry.id)
        );
    }
}
