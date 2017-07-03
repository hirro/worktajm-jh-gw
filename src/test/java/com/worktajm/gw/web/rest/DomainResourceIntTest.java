package com.worktajm.gw.web.rest;

import com.worktajm.gw.WorktajmApp;

import com.worktajm.gw.domain.Domain;
import com.worktajm.gw.repository.DomainRepository;
import com.worktajm.gw.repository.search.DomainSearchRepository;
import com.worktajm.gw.service.dto.DomainDTO;
import com.worktajm.gw.service.mapper.DomainMapper;
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
 * Test class for the DomainResource REST controller.
 *
 * @see DomainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorktajmApp.class)
public class DomainResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private DomainSearchRepository domainSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDomainMockMvc;

    private Domain domain;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DomainResource domainResource = new DomainResource(domainRepository, domainMapper, domainSearchRepository);
        this.restDomainMockMvc = MockMvcBuilders.standaloneSetup(domainResource)
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
    public static Domain createEntity(EntityManager em) {
        Domain domain = new Domain()
            .name(DEFAULT_NAME)
            .domainName(DEFAULT_DOMAIN_NAME)
            .organizationNumber(DEFAULT_ORGANIZATION_NUMBER);
        return domain;
    }

    @Before
    public void initTest() {
        domainSearchRepository.deleteAll();
        domain = createEntity(em);
    }

    @Test
    @Transactional
    public void createDomain() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);
        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isCreated());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate + 1);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomain.getDomainName()).isEqualTo(DEFAULT_DOMAIN_NAME);
        assertThat(testDomain.getOrganizationNumber()).isEqualTo(DEFAULT_ORGANIZATION_NUMBER);

        // Validate the Domain in Elasticsearch
        Domain domainEs = domainSearchRepository.findOne(testDomain.getId());
        assertThat(domainEs).isEqualToComparingFieldByField(testDomain);
    }

    @Test
    @Transactional
    public void createDomainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // Create the Domain with an existing ID
        domain.setId(1L);
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setName(null);

        // Create the Domain, which fails.
        DomainDTO domainDTO = domainMapper.toDto(domain);

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setDomainName(null);

        // Create the Domain, which fails.
        DomainDTO domainDTO = domainMapper.toDto(domain);

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDomains() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList
        restDomainMockMvc.perform(get("/api/domains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME.toString())))
            .andExpect(jsonPath("$.[*].organizationNumber").value(hasItem(DEFAULT_ORGANIZATION_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(domain.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.domainName").value(DEFAULT_DOMAIN_NAME.toString()))
            .andExpect(jsonPath("$.organizationNumber").value(DEFAULT_ORGANIZATION_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDomain() throws Exception {
        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);
        domainSearchRepository.save(domain);
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain
        Domain updatedDomain = domainRepository.findOne(domain.getId());
        updatedDomain
            .name(UPDATED_NAME)
            .domainName(UPDATED_DOMAIN_NAME)
            .organizationNumber(UPDATED_ORGANIZATION_NUMBER);
        DomainDTO domainDTO = domainMapper.toDto(updatedDomain);

        restDomainMockMvc.perform(put("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isOk());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDomain.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
        assertThat(testDomain.getOrganizationNumber()).isEqualTo(UPDATED_ORGANIZATION_NUMBER);

        // Validate the Domain in Elasticsearch
        Domain domainEs = domainSearchRepository.findOne(testDomain.getId());
        assertThat(domainEs).isEqualToComparingFieldByField(testDomain);
    }

    @Test
    @Transactional
    public void updateNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDomainMockMvc.perform(put("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isCreated());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);
        domainSearchRepository.save(domain);
        int databaseSizeBeforeDelete = domainRepository.findAll().size();

        // Get the domain
        restDomainMockMvc.perform(delete("/api/domains/{id}", domain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean domainExistsInEs = domainSearchRepository.exists(domain.getId());
        assertThat(domainExistsInEs).isFalse();

        // Validate the database is empty
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);
        domainSearchRepository.save(domain);

        // Search the domain
        restDomainMockMvc.perform(get("/api/_search/domains?query=id:" + domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME.toString())))
            .andExpect(jsonPath("$.[*].organizationNumber").value(hasItem(DEFAULT_ORGANIZATION_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domain.class);
        Domain domain1 = new Domain();
        domain1.setId(1L);
        Domain domain2 = new Domain();
        domain2.setId(domain1.getId());
        assertThat(domain1).isEqualTo(domain2);
        domain2.setId(2L);
        assertThat(domain1).isNotEqualTo(domain2);
        domain1.setId(null);
        assertThat(domain1).isNotEqualTo(domain2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomainDTO.class);
        DomainDTO domainDTO1 = new DomainDTO();
        domainDTO1.setId(1L);
        DomainDTO domainDTO2 = new DomainDTO();
        assertThat(domainDTO1).isNotEqualTo(domainDTO2);
        domainDTO2.setId(domainDTO1.getId());
        assertThat(domainDTO1).isEqualTo(domainDTO2);
        domainDTO2.setId(2L);
        assertThat(domainDTO1).isNotEqualTo(domainDTO2);
        domainDTO1.setId(null);
        assertThat(domainDTO1).isNotEqualTo(domainDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(domainMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(domainMapper.fromId(null)).isNull();
    }
}
