import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { WorktajmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TimeEntryDetailComponent } from '../../../../../../main/webapp/app/entities/time-entry/time-entry-detail.component';
import { TimeEntryService } from '../../../../../../main/webapp/app/entities/time-entry/time-entry.service';
import { TimeEntry } from '../../../../../../main/webapp/app/entities/time-entry/time-entry.model';

describe('Component Tests', () => {

    describe('TimeEntry Management Detail Component', () => {
        let comp: TimeEntryDetailComponent;
        let fixture: ComponentFixture<TimeEntryDetailComponent>;
        let service: TimeEntryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WorktajmTestModule],
                declarations: [TimeEntryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TimeEntryService,
                    JhiEventManager
                ]
            }).overrideTemplate(TimeEntryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimeEntryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimeEntryService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TimeEntry(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.timeEntry).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
