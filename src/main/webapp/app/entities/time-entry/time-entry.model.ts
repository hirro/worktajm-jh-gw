import { BaseEntity } from './../../shared';

export class TimeEntry implements BaseEntity {
    constructor(
        public id?: number,
        public start?: any,
        public end?: any,
        public comment?: string,
        public projectId?: number,
        public createdById?: number,
    ) {
    }
}
