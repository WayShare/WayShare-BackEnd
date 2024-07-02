package com.voituri.ridesharing.web.rest;

import static com.voituri.ridesharing.domain.MemberAsserts.*;
import static com.voituri.ridesharing.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voituri.ridesharing.IntegrationTest;
import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.repository.MemberRepository;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.mapper.MemberMapper;
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
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemberResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "gPwY}@V{'.xob";
    private static final String UPDATED_EMAIL = "\\EpAa@>)a[.Pr";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    private Member insertedMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .login(DEFAULT_LOGIN)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .email(DEFAULT_EMAIL)
            .activated(DEFAULT_ACTIVATED);
        return member;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .login(UPDATED_LOGIN)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMember != null) {
            memberRepository.delete(insertedMember);
            insertedMember = null;
        }
    }

    @Test
    @Transactional
    void createMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        var returnedMemberDTO = om.readValue(
            restMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemberDTO.class
        );

        // Validate the Member in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMember = memberMapper.toEntity(returnedMemberDTO);
        assertMemberUpdatableFieldsEquals(returnedMember, getPersistedMember(returnedMember));

        insertedMember = returnedMember;
    }

    @Test
    @Transactional
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setLogin(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setPasswordHash(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setEmail(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembers() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    void getMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember.login(UPDATED_LOGIN).passwordHash(UPDATED_PASSWORD_HASH).email(UPDATED_EMAIL).activated(UPDATED_ACTIVATED);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberToMatchAllProperties(updatedMember);
    }

    @Test
    @Transactional
    void putNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember.passwordHash(UPDATED_PASSWORD_HASH).email(UPDATED_EMAIL);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMember, member), getPersistedMember(member));
    }

    @Test
    @Transactional
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember.login(UPDATED_LOGIN).passwordHash(UPDATED_PASSWORD_HASH).email(UPDATED_EMAIL).activated(UPDATED_ACTIVATED);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(partialUpdatedMember, getPersistedMember(partialUpdatedMember));
    }

    @Test
    @Transactional
    void patchNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the member
        restMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, member.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memberRepository.count();
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

    protected Member getPersistedMember(Member member) {
        return memberRepository.findById(member.getId()).orElseThrow();
    }

    protected void assertPersistedMemberToMatchAllProperties(Member expectedMember) {
        assertMemberAllPropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }

    protected void assertPersistedMemberToMatchUpdatableProperties(Member expectedMember) {
        assertMemberAllUpdatablePropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }
}
