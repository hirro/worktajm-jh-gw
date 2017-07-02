package com.worktajm.gw.repository.search;

import com.worktajm.gw.domain.Domain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Domain entity.
 */
public interface DomainSearchRepository extends ElasticsearchRepository<Domain, Long> {
}
