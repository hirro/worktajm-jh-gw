package com.worktajm.gw.service;

import com.worktajm.gw.domain.Worker;
import com.worktajm.gw.repository.WorkerRepository;
import com.worktajm.gw.repository.search.WorkerSearchRepository;
import com.worktajm.gw.service.dto.WorkerDTO;
import com.worktajm.gw.service.mapper.WorkerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Worker.
 */
@Service
@Transactional
public class WorkerService {

    private final Logger log = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerRepository workerRepository;

    private final WorkerMapper workerMapper;

    private final WorkerSearchRepository workerSearchRepository;

    public WorkerService(WorkerRepository workerRepository, WorkerMapper workerMapper, WorkerSearchRepository workerSearchRepository) {
        this.workerRepository = workerRepository;
        this.workerMapper = workerMapper;
        this.workerSearchRepository = workerSearchRepository;
    }

    /**
     * Save a worker.
     *
     * @param workerDTO the entity to save
     * @return the persisted entity
     */
    public WorkerDTO save(WorkerDTO workerDTO) {
        log.debug("Request to save Worker : {}", workerDTO);
        Worker worker = workerMapper.toEntity(workerDTO);
        worker = workerRepository.save(worker);
        WorkerDTO result = workerMapper.toDto(worker);
        workerSearchRepository.save(worker);
        return result;
    }

    /**
     *  Get all the workers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workers");
        return workerRepository.findAll(pageable)
            .map(workerMapper::toDto);
    }

    /**
     *  Get one worker by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WorkerDTO findOne(Long id) {
        log.debug("Request to get Worker : {}", id);
        Worker worker = workerRepository.findOne(id);
        return workerMapper.toDto(worker);
    }

    /**
     *  Delete the  worker by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Worker : {}", id);
        workerRepository.delete(id);
        workerSearchRepository.delete(id);
    }

    /**
     * Search for the worker corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Workers for query {}", query);
        Page<Worker> result = workerSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(workerMapper::toDto);
    }
}
