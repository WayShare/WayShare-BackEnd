package com.voituri.ridesharing.service;

import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.repository.MemberRepository;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.mapper.MemberMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.voituri.ridesharing.domain.Member}.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    /**
     * Save a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberDTO save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        return memberMapper.toDto(member);
    }

    /**
     * Update a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberDTO update(MemberDTO memberDTO) {
        log.debug("Request to update Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        return memberMapper.toDto(member);
    }

    /**
     * Partially update a member.
     *
     * @param memberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MemberDTO> partialUpdate(MemberDTO memberDTO) {
        log.debug("Request to partially update Member : {}", memberDTO);

        return memberRepository
            .findById(memberDTO.getId())
            .map(existingMember -> {
                memberMapper.partialUpdate(existingMember, memberDTO);

                return existingMember;
            })
            .map(memberRepository::save)
            .map(memberMapper::toDto);
    }

    /**
     * Get all the members.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MemberDTO> findAll() {
        log.debug("Request to get all Members");
        return memberRepository.findAll().stream().map(memberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one member by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberDTO> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findById(id).map(memberMapper::toDto);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
    }
}
