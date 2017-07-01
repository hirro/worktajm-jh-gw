package com.worktajm.gw.repository.search;

import com.worktajm.gw.domain.TimeEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TimeEntry entity.
 */
public interface TimeEntrySearchRepository extends ElasticsearchRepository<TimeEntry, Long> {
}
