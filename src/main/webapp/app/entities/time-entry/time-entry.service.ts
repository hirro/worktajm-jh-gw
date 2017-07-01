import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TimeEntryService {

    private resourceUrl = 'api/time-entries';
    private resourceSearchUrl = 'api/_search/time-entries';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(timeEntry: TimeEntry): Observable<TimeEntry> {
        const copy = this.convert(timeEntry);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(timeEntry: TimeEntry): Observable<TimeEntry> {
        const copy = this.convert(timeEntry);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TimeEntry> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.start = this.dateUtils
            .convertDateTimeFromServer(entity.start);
        entity.end = this.dateUtils
            .convertDateTimeFromServer(entity.end);
    }

    private convert(timeEntry: TimeEntry): TimeEntry {
        const copy: TimeEntry = Object.assign({}, timeEntry);

        copy.start = this.dateUtils.toDate(timeEntry.start);

        copy.end = this.dateUtils.toDate(timeEntry.end);
        return copy;
    }
}
