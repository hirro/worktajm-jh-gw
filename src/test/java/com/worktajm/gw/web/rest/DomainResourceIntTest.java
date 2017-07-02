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

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_OR_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_OR_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE_COUNTY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_DETAILS = "BBBBBBBBBB";

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
            .organizationNumber(DEFAULT_ORGANIZATION_NUMBER)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .addressLine3(DEFAULT_ADDRESS_LINE_3)
            .city(DEFAULT_CITY)
            .zipOrPostcode(DEFAULT_ZIP_OR_POSTCODE)
            .stateProvinceCounty(DEFAULT_STATE_PROVINCE_COUNTY)
            .country(DEFAULT_COUNTRY)
            .addressDetails(DEFAULT_ADDRESS_DETAILS);
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
        assertThat(testDomain.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testDomain.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testDomain.getAddressLine3()).isEqualTo(DEFAULT_ADDRESS_LINE_3);
        assertThat(testDomain.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testDomain.getZipOrPostcode()).isEqualTo(DEFAULT_ZIP_OR_POSTCODE);
        assertThat(testDomain.getStateProvinceCounty()).isEqualTo(DEFAULT_STATE_PROVINCE_COUNTY);
        assertThat(testDomain.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testDomain.getAddressDetails()).isEqualTo(DEFAULT_ADDRESS_DETAILS);

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
    public void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setAddressLine1(null);

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
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setCity(null);

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
    public void checkZipOrPostcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setZipOrPostcode(null);

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
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setCountry(null);

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
            .andExpect(jsonPath("$.[*].organizationNumber").value(hasItem(DEFAULT_ORGANIZATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].addressLine3").value(hasItem(DEFAULT_ADDRESS_LINE_3.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipOrPostcode").value(hasItem(DEFAULT_ZIP_OR_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].stateProvinceCounty").value(hasItem(DEFAULT_STATE_PROVINCE_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].addressDetails").value(hasItem(DEFAULT_ADDRESS_DETAILS.toString())));
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
            .andExpect(jsonPath("$.organizationNumber").value(DEFAULT_ORGANIZATION_NUMBER.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1.toString()))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2.toString()))
            .andExpect(jsonPath("$.addressLine3").value(DEFAULT_ADDRESS_LINE_3.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipOrPostcode").value(DEFAULT_ZIP_OR_POSTCODE.toString()))
            .andExpect(jsonPath("$.stateProvinceCounty").value(DEFAULT_STATE_PROVINCE_COUNTY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.addressDetails").value(DEFAULT_ADDRESS_DETAILS.toString()));
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
            .organizationNumber(UPDATED_ORGANIZATION_NUMBER)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .addressLine3(UPDATED_ADDRESS_LINE_3)
            .city(UPDATED_CITY)
            .zipOrPostcode(UPDATED_ZIP_OR_POSTCODE)
            .stateProvinceCounty(UPDATED_STATE_PROVINCE_COUNTY)
            .country(UPDATED_COUNTRY)
            .addressDetails(UPDATED_ADDRESS_DETAILS);
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
        assertThat(testDomain.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testDomain.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testDomain.getAddressLine3()).isEqualTo(UPDATED_ADDRESS_LINE_3);
        assertThat(testDomain.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testDomain.getZipOrPostcode()).isEqualTo(UPDATED_ZIP_OR_POSTCODE);
        assertThat(testDomain.getStateProvinceCounty()).isEqualTo(UPDATED_STATE_PROVINCE_COUNTY);
        assertThat(testDomain.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testDomain.getAddressDetails()).isEqualTo(UPDATED_ADDRESS_DETAILS);

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
            .andExpect(jsonPath("$.[*].organizationNumber").value(hasItem(DEFAULT_ORGANIZATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].addressLine3").value(hasItem(DEFAULT_ADDRESS_LINE_3.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipOrPostcode").value(hasItem(DEFAULT_ZIP_OR_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].stateProvinceCounty").value(hasItem(DEFAULT_STATE_PROVINCE_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].addressDetails").value(hasItem(DEFAULT_ADDRESS_DETAILS.toString())));
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
