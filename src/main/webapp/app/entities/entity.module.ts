import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WorktajmGwWorkerModule } from './worker/worker.module';
import { WorktajmGwProjectModule } from './project/project.module';
import { WorktajmGwTimeEntryModule } from './time-entry/time-entry.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WorktajmGwWorkerModule,
        WorktajmGwProjectModule,
        WorktajmGwTimeEntryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorktajmGwEntityModule {}
