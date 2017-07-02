import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { TimeEntryPopupService } from './time-entry-popup.service';
import { TimeEntryService } from './time-entry.service';
import { Project, ProjectService } from '../project';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-entry-dialog',
    templateUrl: './time-entry-dialog.component.html'
})
export class TimeEntryDialogComponent implements OnInit {

    timeEntry: TimeEntry;
    authorities: any[];
    isSaving: boolean;

    projects: Project[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private timeEntryService: TimeEntryService,
        private projectService: ProjectService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
            isCreated ? 'worktajmApp.timeEntry.created'
            : 'worktajmApp.timeEntry.updated',
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

    trackProjectById(index: number, item: Project) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
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
