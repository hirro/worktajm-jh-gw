import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WorktajmGwTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WorkerDetailComponent } from '../../../../../../main/webapp/app/entities/worker/worker-detail.component';
import { WorkerService } from '../../../../../../main/webapp/app/entities/worker/worker.service';
import { Worker } from '../../../../../../main/webapp/app/entities/worker/worker.model';

describe('Component Tests', () => {

    describe('Worker Management Detail Component', () => {
        let comp: WorkerDetailComponent;
        let fixture: ComponentFixture<WorkerDetailComponent>;
        let service: WorkerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WorktajmGwTestModule],
                declarations: [WorkerDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WorkerService,
                    EventManager
                ]
            }).overrideTemplate(WorkerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WorkerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkerService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Worker(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.worker).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
