import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public addressLine1?: string,
        public addressLine2?: string,
        public addressLine3?: string,
        public city?: string,
        public zipOrPostcode?: string,
        public stateProvinceCounty?: string,
        public country?: string,
        public addressDetails?: string,
        public projects?: BaseEntity[],
        public domainId?: number,
    ) {
    }
}
