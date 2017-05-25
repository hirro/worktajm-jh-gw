package com.worktajm.gw.repository.search;

import com.worktajm.gw.domain.Worker;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Worker entity.
 */
public interface WorkerSearchRepository extends ElasticsearchRepository<Worker, Long> {
}
