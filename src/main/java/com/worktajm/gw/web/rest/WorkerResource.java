package com.worktajm.gw.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.worktajm.gw.service.WorkerService;
import com.worktajm.gw.web.rest.util.HeaderUtil;
import com.worktajm.gw.web.rest.util.PaginationUtil;
import com.worktajm.gw.service.dto.WorkerDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Worker.
 */
@RestController
@RequestMapping("/api")
public class WorkerResource {

    private final Logger log = LoggerFactory.getLogger(WorkerResource.class);

    private static final String ENTITY_NAME = "worker";
        
    private final WorkerService workerService;

    public WorkerResource(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * POST  /workers : Create a new worker.
     *
     * @param workerDTO the workerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workerDTO, or with status 400 (Bad Request) if the worker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workers")
    @Timed
    public ResponseEntity<WorkerDTO> createWorker(@RequestBody WorkerDTO workerDTO) throws URISyntaxException {
        log.debug("REST request to save Worker : {}", workerDTO);
        if (workerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new worker cannot already have an ID")).body(null);
        }
        WorkerDTO result = workerService.save(workerDTO);
        return ResponseEntity.created(new URI("/api/workers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workers : Updates an existing worker.
     *
     * @param workerDTO the workerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workerDTO,
     * or with status 400 (Bad Request) if the workerDTO is not valid,
     * or with status 500 (Internal Server Error) if the workerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workers")
    @Timed
    public ResponseEntity<WorkerDTO> updateWorker(@RequestBody WorkerDTO workerDTO) throws URISyntaxException {
        log.debug("REST request to update Worker : {}", workerDTO);
        if (workerDTO.getId() == null) {
            return createWorker(workerDTO);
        }
        WorkerDTO result = workerService.save(workerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workers : get all the workers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workers in body
     */
    @GetMapping("/workers")
    @Timed
    public ResponseEntity<List<WorkerDTO>> getAllWorkers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Workers");
        Page<WorkerDTO> page = workerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workers/:id : get the "id" worker.
     *
     * @param id the id of the workerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workers/{id}")
    @Timed
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable Long id) {
        log.debug("REST request to get Worker : {}", id);
        WorkerDTO workerDTO = workerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workerDTO));
    }

    /**
     * DELETE  /workers/:id : delete the "id" worker.
     *
     * @param id the id of the workerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        log.debug("REST request to delete Worker : {}", id);
        workerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/workers?query=:query : search for the worker corresponding
     * to the query.
     *
     * @param query the query of the worker search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/workers")
    @Timed
    public ResponseEntity<List<WorkerDTO>> searchWorkers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Workers for query {}", query);
        Page<WorkerDTO> page = workerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
