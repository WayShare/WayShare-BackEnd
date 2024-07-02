package com.voituri.ridesharing.service;

import com.voituri.ridesharing.domain.Rating;
import com.voituri.ridesharing.repository.RatingRepository;
import com.voituri.ridesharing.service.dto.RatingDTO;
import com.voituri.ridesharing.service.mapper.RatingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.voituri.ridesharing.domain.Rating}.
 */
@Service
@Transactional
public class RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingService.class);

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    /**
     * Save a rating.
     *
     * @param ratingDTO the entity to save.
     * @return the persisted entity.
     */
    public RatingDTO save(RatingDTO ratingDTO) {
        log.debug("Request to save Rating : {}", ratingDTO);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    /**
     * Update a rating.
     *
     * @param ratingDTO the entity to save.
     * @return the persisted entity.
     */
    public RatingDTO update(RatingDTO ratingDTO) {
        log.debug("Request to update Rating : {}", ratingDTO);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    /**
     * Partially update a rating.
     *
     * @param ratingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RatingDTO> partialUpdate(RatingDTO ratingDTO) {
        log.debug("Request to partially update Rating : {}", ratingDTO);

        return ratingRepository
            .findById(ratingDTO.getId())
            .map(existingRating -> {
                ratingMapper.partialUpdate(existingRating, ratingDTO);

                return existingRating;
            })
            .map(ratingRepository::save)
            .map(ratingMapper::toDto);
    }

    /**
     * Get all the ratings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RatingDTO> findAll() {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll().stream().map(ratingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rating by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RatingDTO> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id).map(ratingMapper::toDto);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
    }
}
