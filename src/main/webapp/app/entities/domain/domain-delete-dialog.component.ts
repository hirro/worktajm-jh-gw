import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Domain } from './domain.model';
import { DomainPopupService } from './domain-popup.service';
import { DomainService } from './domain.service';

@Component({
    selector: 'jhi-domain-delete-dialog',
    templateUrl: './domain-delete-dialog.component.html'
})
export class DomainDeleteDialogComponent {

    domain: Domain;

    constructor(
        private domainService: DomainService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.domainService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'domainListModification',
                content: 'Deleted an domain'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('worktajmApp.domain.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-domain-delete-popup',
    template: ''
})
export class DomainDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private domainPopupService: DomainPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.domainPopupService
                .open(DomainDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
