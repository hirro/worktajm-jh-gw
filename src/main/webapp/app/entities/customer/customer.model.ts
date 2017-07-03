import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public addressId?: number,
        public projects?: BaseEntity[],
        public domainId?: number,
    ) {
    }
}
