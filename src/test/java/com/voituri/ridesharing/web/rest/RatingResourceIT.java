package com.voituri.ridesharing.web.rest;

import static com.voituri.ridesharing.domain.RatingAsserts.*;
import static com.voituri.ridesharing.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voituri.ridesharing.IntegrationTest;
import com.voituri.ridesharing.domain.Rating;
import com.voituri.ridesharing.repository.RatingRepository;
import com.voituri.ridesharing.service.dto.RatingDTO;
import com.voituri.ridesharing.service.mapper.RatingMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link RatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingResourceIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingMockMvc;

    private Rating rating;

    private Rating insertedRating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating().score(DEFAULT_SCORE).feedback(DEFAULT_FEEDBACK);
        return rating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createUpdatedEntity(EntityManager em) {
        Rating rating = new Rating().score(UPDATED_SCORE).feedback(UPDATED_FEEDBACK);
        return rating;
    }

    @BeforeEach
    public void initTest() {
        rating = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRating != null) {
            ratingRepository.delete(insertedRating);
            insertedRating = null;
        }
    }

    @Test
    @Transactional
    void createRating() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);
        var returnedRatingDTO = om.readValue(
            restRatingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RatingDTO.class
        );

        // Validate the Rating in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRating = ratingMapper.toEntity(returnedRatingDTO);
        assertRatingUpdatableFieldsEquals(returnedRating, getPersistedRating(returnedRating));

        insertedRating = returnedRating;
    }

    @Test
    @Transactional
    void createRatingWithExistingId() throws Exception {
        // Create the Rating with an existing ID
        rating.setId(1L);
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setScore(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRatings() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK)));
    }

    @Test
    @Transactional
    void getRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc
            .perform(get(ENTITY_API_URL_ID, rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK));
    }

    @Test
    @Transactional
    void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating.score(UPDATED_SCORE).feedback(UPDATED_FEEDBACK);
        RatingDTO ratingDTO = ratingMapper.toDto(updatedRating);

        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRatingToMatchAllProperties(updatedRating);
    }

    @Test
    @Transactional
    void putNonExistingRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating.score(UPDATED_SCORE).feedback(UPDATED_FEEDBACK);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRating, rating), getPersistedRating(rating));
    }

    @Test
    @Transactional
    void fullUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating.score(UPDATED_SCORE).feedback(UPDATED_FEEDBACK);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingUpdatableFieldsEquals(partialUpdatedRating, getPersistedRating(partialUpdatedRating));
    }

    @Test
    @Transactional
    void patchNonExistingRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rating
        restRatingMockMvc
            .perform(delete(ENTITY_API_URL_ID, rating.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ratingRepository.count();
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

    protected Rating getPersistedRating(Rating rating) {
        return ratingRepository.findById(rating.getId()).orElseThrow();
    }

    protected void assertPersistedRatingToMatchAllProperties(Rating expectedRating) {
        assertRatingAllPropertiesEquals(expectedRating, getPersistedRating(expectedRating));
    }

    protected void assertPersistedRatingToMatchUpdatableProperties(Rating expectedRating) {
        assertRatingAllUpdatablePropertiesEquals(expectedRating, getPersistedRating(expectedRating));
    }
}
