package com.voituri.ridesharing.web.rest;

import static com.voituri.ridesharing.domain.RideRequestAsserts.*;
import static com.voituri.ridesharing.web.rest.TestUtil.createUpdateProxyForBean;
import static com.voituri.ridesharing.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voituri.ridesharing.IntegrationTest;
import com.voituri.ridesharing.domain.RideRequest;
import com.voituri.ridesharing.repository.RideRequestRepository;
import com.voituri.ridesharing.service.dto.RideRequestDTO;
import com.voituri.ridesharing.service.mapper.RideRequestMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RideRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RideRequestResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ride-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private RideRequestMapper rideRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRideRequestMockMvc;

    private RideRequest rideRequest;

    private RideRequest insertedRideRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RideRequest createEntity(EntityManager em) {
        RideRequest rideRequest = new RideRequest().status(DEFAULT_STATUS).requestTime(DEFAULT_REQUEST_TIME);
        return rideRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RideRequest createUpdatedEntity(EntityManager em) {
        RideRequest rideRequest = new RideRequest().status(UPDATED_STATUS).requestTime(UPDATED_REQUEST_TIME);
        return rideRequest;
    }

    @BeforeEach
    public void initTest() {
        rideRequest = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRideRequest != null) {
            rideRequestRepository.delete(insertedRideRequest);
            insertedRideRequest = null;
        }
    }

    @Test
    @Transactional
    void createRideRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);
        var returnedRideRequestDTO = om.readValue(
            restRideRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RideRequestDTO.class
        );

        // Validate the RideRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRideRequest = rideRequestMapper.toEntity(returnedRideRequestDTO);
        assertRideRequestUpdatableFieldsEquals(returnedRideRequest, getPersistedRideRequest(returnedRideRequest));

        insertedRideRequest = returnedRideRequest;
    }

    @Test
    @Transactional
    void createRideRequestWithExistingId() throws Exception {
        // Create the RideRequest with an existing ID
        rideRequest.setId(1L);
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rideRequest.setStatus(null);

        // Create the RideRequest, which fails.
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        restRideRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rideRequest.setRequestTime(null);

        // Create the RideRequest, which fails.
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        restRideRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRideRequests() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        // Get all the rideRequestList
        restRideRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rideRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(sameInstant(DEFAULT_REQUEST_TIME))));
    }

    @Test
    @Transactional
    void getRideRequest() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        // Get the rideRequest
        restRideRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, rideRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rideRequest.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.requestTime").value(sameInstant(DEFAULT_REQUEST_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingRideRequest() throws Exception {
        // Get the rideRequest
        restRideRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRideRequest() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rideRequest
        RideRequest updatedRideRequest = rideRequestRepository.findById(rideRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRideRequest are not directly saved in db
        em.detach(updatedRideRequest);
        updatedRideRequest.status(UPDATED_STATUS).requestTime(UPDATED_REQUEST_TIME);
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(updatedRideRequest);

        restRideRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rideRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rideRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRideRequestToMatchAllProperties(updatedRideRequest);
    }

    @Test
    @Transactional
    void putNonExistingRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rideRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rideRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rideRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRideRequestWithPatch() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rideRequest using partial update
        RideRequest partialUpdatedRideRequest = new RideRequest();
        partialUpdatedRideRequest.setId(rideRequest.getId());

        partialUpdatedRideRequest.requestTime(UPDATED_REQUEST_TIME);

        restRideRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRideRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRideRequest))
            )
            .andExpect(status().isOk());

        // Validate the RideRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRideRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRideRequest, rideRequest),
            getPersistedRideRequest(rideRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateRideRequestWithPatch() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rideRequest using partial update
        RideRequest partialUpdatedRideRequest = new RideRequest();
        partialUpdatedRideRequest.setId(rideRequest.getId());

        partialUpdatedRideRequest.status(UPDATED_STATUS).requestTime(UPDATED_REQUEST_TIME);

        restRideRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRideRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRideRequest))
            )
            .andExpect(status().isOk());

        // Validate the RideRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRideRequestUpdatableFieldsEquals(partialUpdatedRideRequest, getPersistedRideRequest(partialUpdatedRideRequest));
    }

    @Test
    @Transactional
    void patchNonExistingRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rideRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rideRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rideRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRideRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rideRequest.setId(longCount.incrementAndGet());

        // Create the RideRequest
        RideRequestDTO rideRequestDTO = rideRequestMapper.toDto(rideRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rideRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RideRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRideRequest() throws Exception {
        // Initialize the database
        insertedRideRequest = rideRequestRepository.saveAndFlush(rideRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rideRequest
        restRideRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, rideRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rideRequestRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected RideRequest getPersistedRideRequest(RideRequest rideRequest) {
        return rideRequestRepository.findById(rideRequest.getId()).orElseThrow();
    }

    protected void assertPersistedRideRequestToMatchAllProperties(RideRequest expectedRideRequest) {
        assertRideRequestAllPropertiesEquals(expectedRideRequest, getPersistedRideRequest(expectedRideRequest));
    }

    protected void assertPersistedRideRequestToMatchUpdatableProperties(RideRequest expectedRideRequest) {
        assertRideRequestAllUpdatablePropertiesEquals(expectedRideRequest, getPersistedRideRequest(expectedRideRequest));
    }
}
