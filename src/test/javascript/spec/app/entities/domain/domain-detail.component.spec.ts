import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { WorktajmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DomainDetailComponent } from '../../../../../../main/webapp/app/entities/domain/domain-detail.component';
import { DomainService } from '../../../../../../main/webapp/app/entities/domain/domain.service';
import { Domain } from '../../../../../../main/webapp/app/entities/domain/domain.model';

describe('Component Tests', () => {

    describe('Domain Management Detail Component', () => {
        let comp: DomainDetailComponent;
        let fixture: ComponentFixture<DomainDetailComponent>;
        let service: DomainService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WorktajmTestModule],
                declarations: [DomainDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DomainService,
                    JhiEventManager
                ]
            }).overrideTemplate(DomainDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DomainDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DomainService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Domain(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.domain).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
