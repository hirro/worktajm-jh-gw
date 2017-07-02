import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Domain } from './domain.model';
import { DomainService } from './domain.service';

@Component({
    selector: 'jhi-domain-detail',
    templateUrl: './domain-detail.component.html'
})
export class DomainDetailComponent implements OnInit, OnDestroy {

    domain: Domain;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private domainService: DomainService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDomains();
    }

    load(id) {
        this.domainService.find(id).subscribe((domain) => {
            this.domain = domain;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDomains() {
        this.eventSubscriber = this.eventManager.subscribe(
            'domainListModification',
            (response) => this.load(this.domain.id)
        );
    }
}
