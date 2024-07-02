package com.voituri.ridesharing.web.rest;

import static com.voituri.ridesharing.domain.ProfileAsserts.*;
import static com.voituri.ridesharing.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voituri.ridesharing.IntegrationTest;
import com.voituri.ridesharing.domain.Profile;
import com.voituri.ridesharing.repository.ProfileRepository;
import com.voituri.ridesharing.service.dto.ProfileDTO;
import com.voituri.ridesharing.service.mapper.ProfileMapper;
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
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfileResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileMockMvc;

    private Profile profile;

    private Profile insertedProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .photo(DEFAULT_PHOTO)
            .contactDetails(DEFAULT_CONTACT_DETAILS);
        return profile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .photo(UPDATED_PHOTO)
            .contactDetails(UPDATED_CONTACT_DETAILS);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProfile != null) {
            profileRepository.delete(insertedProfile);
            insertedProfile = null;
        }
    }

    @Test
    @Transactional
    void createProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        var returnedProfileDTO = om.readValue(
            restProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfileDTO.class
        );

        // Validate the Profile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfile = profileMapper.toEntity(returnedProfileDTO);
        assertProfileUpdatableFieldsEquals(returnedProfile, getPersistedProfile(returnedProfile));

        insertedProfile = returnedProfile;
    }

    @Test
    @Transactional
    void createProfileWithExistingId() throws Exception {
        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfiles() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)));
    }

    @Test
    @Transactional
    void getProfile() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.contactDetails").value(DEFAULT_CONTACT_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfile() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .photo(UPDATED_PHOTO)
            .contactDetails(UPDATED_CONTACT_DETAILS);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfileToMatchAllProperties(updatedProfile);
    }

    @Test
    @Transactional
    void putNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).photo(UPDATED_PHOTO);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfile, profile), getPersistedProfile(profile));
    }

    @Test
    @Transactional
    void fullUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .photo(UPDATED_PHOTO)
            .contactDetails(UPDATED_CONTACT_DETAILS);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(partialUpdatedProfile, getPersistedProfile(partialUpdatedProfile));
    }

    @Test
    @Transactional
    void patchNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfile() throws Exception {
        // Initialize the database
        insertedProfile = profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profile
        restProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, profile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profileRepository.count();
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

    protected Profile getPersistedProfile(Profile profile) {
        return profileRepository.findById(profile.getId()).orElseThrow();
    }

    protected void assertPersistedProfileToMatchAllProperties(Profile expectedProfile) {
        assertProfileAllPropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }

    protected void assertPersistedProfileToMatchUpdatableProperties(Profile expectedProfile) {
        assertProfileAllUpdatablePropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }
}
