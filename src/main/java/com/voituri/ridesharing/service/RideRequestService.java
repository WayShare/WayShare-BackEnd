package com.voituri.ridesharing.service;

import com.voituri.ridesharing.domain.RideRequest;
import com.voituri.ridesharing.repository.RideRequestRepository;
import com.voituri.ridesharing.service.dto.RideRequestDTO;
import com.voituri.ridesharing.service.mapper.RideRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.voituri.ridesharing.domain.RideRequest}.
 */
@Service
@Transactional
public class RideRequestService {

    private final Logger log = LoggerFactory.getLogger(RideRequestService.class);

    private final RideRequestRepository rideRequestRepository;

    private final RideRequestMapper rideRequestMapper;

    public RideRequestService(RideRequestRepository rideRequestRepository, RideRequestMapper rideRequestMapper) {
        this.rideRequestRepository = rideRequestRepository;
        this.rideRequestMapper = rideRequestMapper;
    }

    /**
     * Save a rideRequest.
     *
     * @param rideRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public RideRequestDTO save(RideRequestDTO rideRequestDTO) {
        log.debug("Request to save RideRequest : {}", rideRequestDTO);
        RideRequest rideRequest = rideRequestMapper.toEntity(rideRequestDTO);
        rideRequest = rideRequestRepository.save(rideRequest);
        return rideRequestMapper.toDto(rideRequest);
    }

    /**
     * Update a rideRequest.
     *
     * @param rideRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public RideRequestDTO update(RideRequestDTO rideRequestDTO) {
        log.debug("Request to update RideRequest : {}", rideRequestDTO);
        RideRequest rideRequest = rideRequestMapper.toEntity(rideRequestDTO);
        rideRequest = rideRequestRepository.save(rideRequest);
        return rideRequestMapper.toDto(rideRequest);
    }

    /**
     * Partially update a rideRequest.
     *
     * @param rideRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RideRequestDTO> partialUpdate(RideRequestDTO rideRequestDTO) {
        log.debug("Request to partially update RideRequest : {}", rideRequestDTO);

        return rideRequestRepository
            .findById(rideRequestDTO.getId())
            .map(existingRideRequest -> {
                rideRequestMapper.partialUpdate(existingRideRequest, rideRequestDTO);

                return existingRideRequest;
            })
            .map(rideRequestRepository::save)
            .map(rideRequestMapper::toDto);
    }

    /**
     * Get all the rideRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RideRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RideRequests");
        return rideRequestRepository.findAll(pageable).map(rideRequestMapper::toDto);
    }

    /**
     * Get one rideRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RideRequestDTO> findOne(Long id) {
        log.debug("Request to get RideRequest : {}", id);
        return rideRequestRepository.findById(id).map(rideRequestMapper::toDto);
    }

    /**
     * Delete the rideRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RideRequest : {}", id);
        rideRequestRepository.deleteById(id);
    }
}
