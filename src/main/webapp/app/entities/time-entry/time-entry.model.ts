export class TimeEntry {
    constructor(
        public id?: number,
        public start?: any,
        public end?: any,
        public comment?: string,
        public workerId?: number,
        public projectId?: number,
    ) {
    }
}
