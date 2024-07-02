package com.voituri.ridesharing.web.rest;

import static com.voituri.ridesharing.domain.RideAsserts.*;
import static com.voituri.ridesharing.web.rest.TestUtil.createUpdateProxyForBean;
import static com.voituri.ridesharing.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voituri.ridesharing.IntegrationTest;
import com.voituri.ridesharing.domain.Ride;
import com.voituri.ridesharing.repository.RideRepository;
import com.voituri.ridesharing.service.dto.RideDTO;
import com.voituri.ridesharing.service.mapper.RideMapper;
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
 * Integration tests for the {@link RideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RideResourceIT {

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_RECURRING = false;
    private static final Boolean UPDATED_RECURRING = true;

    private static final String ENTITY_API_URL = "/api/rides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideMapper rideMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRideMockMvc;

    private Ride ride;

    private Ride insertedRide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .startLocation(DEFAULT_START_LOCATION)
            .endLocation(DEFAULT_END_LOCATION)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .recurring(DEFAULT_RECURRING);
        return ride;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createUpdatedEntity(EntityManager em) {
        Ride ride = new Ride()
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .recurring(UPDATED_RECURRING);
        return ride;
    }

    @BeforeEach
    public void initTest() {
        ride = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRide != null) {
            rideRepository.delete(insertedRide);
            insertedRide = null;
        }
    }

    @Test
    @Transactional
    void createRide() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);
        var returnedRideDTO = om.readValue(
            restRideMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RideDTO.class
        );

        // Validate the Ride in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRide = rideMapper.toEntity(returnedRideDTO);
        assertRideUpdatableFieldsEquals(returnedRide, getPersistedRide(returnedRide));

        insertedRide = returnedRide;
    }

    @Test
    @Transactional
    void createRideWithExistingId() throws Exception {
        // Create the Ride with an existing ID
        ride.setId(1L);
        RideDTO rideDTO = rideMapper.toDto(ride);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ride.setStartLocation(null);

        // Create the Ride, which fails.
        RideDTO rideDTO = rideMapper.toDto(ride);

        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ride.setEndLocation(null);

        // Create the Ride, which fails.
        RideDTO rideDTO = rideMapper.toDto(ride);

        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ride.setStartTime(null);

        // Create the Ride, which fails.
        RideDTO rideDTO = rideMapper.toDto(ride);

        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRides() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].recurring").value(hasItem(DEFAULT_RECURRING.booleanValue())));
    }

    @Test
    @Transactional
    void getRide() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc
            .perform(get(ENTITY_API_URL_ID, ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION))
            .andExpect(jsonPath("$.endLocation").value(DEFAULT_END_LOCATION))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.recurring").value(DEFAULT_RECURRING.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRide() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ride
        Ride updatedRide = rideRepository.findById(ride.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRide are not directly saved in db
        em.detach(updatedRide);
        updatedRide
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .recurring(UPDATED_RECURRING);
        RideDTO rideDTO = rideMapper.toDto(updatedRide);

        restRideMockMvc
            .perform(put(ENTITY_API_URL_ID, rideDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isOk());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRideToMatchAllProperties(updatedRide);
    }

    @Test
    @Transactional
    void putNonExistingRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(put(ENTITY_API_URL_ID, rideDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRideWithPatch() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ride using partial update
        Ride partialUpdatedRide = new Ride();
        partialUpdatedRide.setId(ride.getId());

        partialUpdatedRide
            .endLocation(UPDATED_END_LOCATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .recurring(UPDATED_RECURRING);

        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRide))
            )
            .andExpect(status().isOk());

        // Validate the Ride in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRideUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRide, ride), getPersistedRide(ride));
    }

    @Test
    @Transactional
    void fullUpdateRideWithPatch() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ride using partial update
        Ride partialUpdatedRide = new Ride();
        partialUpdatedRide.setId(ride.getId());

        partialUpdatedRide
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .recurring(UPDATED_RECURRING);

        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRide))
            )
            .andExpect(status().isOk());

        // Validate the Ride in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRideUpdatableFieldsEquals(partialUpdatedRide, getPersistedRide(partialUpdatedRide));
    }

    @Test
    @Transactional
    void patchNonExistingRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rideDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ride.setId(longCount.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ride in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRide() throws Exception {
        // Initialize the database
        insertedRide = rideRepository.saveAndFlush(ride);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ride
        restRideMockMvc
            .perform(delete(ENTITY_API_URL_ID, ride.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rideRepository.count();
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

    protected Ride getPersistedRide(Ride ride) {
        return rideRepository.findById(ride.getId()).orElseThrow();
    }

    protected void assertPersistedRideToMatchAllProperties(Ride expectedRide) {
        assertRideAllPropertiesEquals(expectedRide, getPersistedRide(expectedRide));
    }

    protected void assertPersistedRideToMatchUpdatableProperties(Ride expectedRide) {
        assertRideAllUpdatablePropertiesEquals(expectedRide, getPersistedRide(expectedRide));
    }
}
