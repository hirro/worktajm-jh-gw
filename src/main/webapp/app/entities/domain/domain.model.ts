import { BaseEntity, User } from './../../shared';

export class Domain implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public domainName?: string,
        public organizationNumber?: string,
        public addressLine1?: string,
        public addressLine2?: string,
        public addressLine3?: string,
        public city?: string,
        public zipOrPostcode?: string,
        public stateProvinceCounty?: string,
        public country?: string,
        public addressDetails?: string,
        public customers?: BaseEntity[],
        public members?: User[],
    ) {
    }
}
