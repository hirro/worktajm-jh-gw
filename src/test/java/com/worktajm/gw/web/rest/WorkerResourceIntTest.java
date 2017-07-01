package com.worktajm.gw.web.rest;

import com.worktajm.gw.WorktajmGwApp;

import com.worktajm.gw.domain.Worker;
import com.worktajm.gw.repository.WorkerRepository;
import com.worktajm.gw.service.WorkerService;
import com.worktajm.gw.repository.search.WorkerSearchRepository;
import com.worktajm.gw.service.dto.WorkerDTO;
import com.worktajm.gw.service.mapper.WorkerMapper;
import com.worktajm.gw.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkerResource REST controller.
 *
 * @see WorkerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorktajmGwApp.class)
public class WorkerResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerSearchRepository workerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkerMockMvc;

    private Worker worker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkerResource workerResource = new WorkerResource(workerService);
        this.restWorkerMockMvc = MockMvcBuilders.standaloneSetup(workerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worker createEntity(EntityManager em) {
        Worker worker = new Worker()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL);
        return worker;
    }

    @Before
    public void initTest() {
        workerSearchRepository.deleteAll();
        worker = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorker() throws Exception {
        int databaseSizeBeforeCreate = workerRepository.findAll().size();

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);
        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isCreated());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate + 1);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testWorker.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testWorker.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Worker in Elasticsearch
        Worker workerEs = workerSearchRepository.findOne(testWorker.getId());
        assertThat(workerEs).isEqualToComparingFieldByField(testWorker);
    }

    @Test
    @Transactional
    public void createWorkerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workerRepository.findAll().size();

        // Create the Worker with an existing ID
        worker.setId(1L);
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWorkers() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList
        restWorkerMockMvc.perform(get("/api/workers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get the worker
        restWorkerMockMvc.perform(get("/api/workers/{id}", worker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(worker.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorker() throws Exception {
        // Get the worker
        restWorkerMockMvc.perform(get("/api/workers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);
        workerSearchRepository.save(worker);
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Update the worker
        Worker updatedWorker = workerRepository.findOne(worker.getId());
        updatedWorker
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        WorkerDTO workerDTO = workerMapper.toDto(updatedWorker);

        restWorkerMockMvc.perform(put("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isOk());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWorker.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testWorker.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Worker in Elasticsearch
        Worker workerEs = workerSearchRepository.findOne(testWorker.getId());
        assertThat(workerEs).isEqualToComparingFieldByField(testWorker);
    }

    @Test
    @Transactional
    public void updateNonExistingWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkerMockMvc.perform(put("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isCreated());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);
        workerSearchRepository.save(worker);
        int databaseSizeBeforeDelete = workerRepository.findAll().size();

        // Get the worker
        restWorkerMockMvc.perform(delete("/api/workers/{id}", worker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean workerExistsInEs = workerSearchRepository.exists(worker.getId());
        assertThat(workerExistsInEs).isFalse();

        // Validate the database is empty
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);
        workerSearchRepository.save(worker);

        // Search the worker
        restWorkerMockMvc.perform(get("/api/_search/workers?query=id:" + worker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Worker.class);
        Worker worker1 = new Worker();
        worker1.setId(1L);
        Worker worker2 = new Worker();
        worker2.setId(worker1.getId());
        assertThat(worker1).isEqualTo(worker2);
        worker2.setId(2L);
        assertThat(worker1).isNotEqualTo(worker2);
        worker1.setId(null);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkerDTO.class);
        WorkerDTO workerDTO1 = new WorkerDTO();
        workerDTO1.setId(1L);
        WorkerDTO workerDTO2 = new WorkerDTO();
        assertThat(workerDTO1).isNotEqualTo(workerDTO2);
        workerDTO2.setId(workerDTO1.getId());
        assertThat(workerDTO1).isEqualTo(workerDTO2);
        workerDTO2.setId(2L);
        assertThat(workerDTO1).isNotEqualTo(workerDTO2);
        workerDTO1.setId(null);
        assertThat(workerDTO1).isNotEqualTo(workerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workerMapper.fromId(null)).isNull();
    }
}
