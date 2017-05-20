package com.arnellconsulting.worktajm.gw.repository.search;

import com.arnellconsulting.worktajm.gw.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
