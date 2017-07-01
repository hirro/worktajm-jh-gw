import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { TimeEntryPopupService } from './time-entry-popup.service';
import { TimeEntryService } from './time-entry.service';
import { Worker, WorkerService } from '../worker';
import { Project, ProjectService } from '../project';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-entry-dialog',
    templateUrl: './time-entry-dialog.component.html'
})
export class TimeEntryDialogComponent implements OnInit {

    timeEntry: TimeEntry;
    authorities: any[];
    isSaving: boolean;

    workers: Worker[];

    projects: Project[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private timeEntryService: TimeEntryService,
        private workerService: WorkerService,
        private projectService: ProjectService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.workerService.query()
            .subscribe((res: ResponseWrapper) => { this.workers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timeEntry.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timeEntryService.update(this.timeEntry), false);
        } else {
            this.subscribeToSaveResponse(
                this.timeEntryService.create(this.timeEntry), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TimeEntry>, isCreated: boolean) {
        result.subscribe((res: TimeEntry) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TimeEntry, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'worktajmGwApp.timeEntry.created'
            : 'worktajmGwApp.timeEntry.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'timeEntryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackWorkerById(index: number, item: Worker) {
        return item.id;
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-time-entry-popup',
    template: ''
})
export class TimeEntryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeEntryPopupService: TimeEntryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.timeEntryPopupService
                    .open(TimeEntryDialogComponent, params['id']);
            } else {
                this.modalRef = this.timeEntryPopupService
                    .open(TimeEntryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
