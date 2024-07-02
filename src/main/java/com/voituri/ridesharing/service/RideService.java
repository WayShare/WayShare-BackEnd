package com.voituri.ridesharing.service;

import com.voituri.ridesharing.domain.Ride;
import com.voituri.ridesharing.repository.RideRepository;
import com.voituri.ridesharing.service.dto.RideDTO;
import com.voituri.ridesharing.service.mapper.RideMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.voituri.ridesharing.domain.Ride}.
 */
@Service
@Transactional
public class RideService {

    private final Logger log = LoggerFactory.getLogger(RideService.class);

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    public RideService(RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
    }

    /**
     * Save a ride.
     *
     * @param rideDTO the entity to save.
     * @return the persisted entity.
     */
    public RideDTO save(RideDTO rideDTO) {
        log.debug("Request to save Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    /**
     * Update a ride.
     *
     * @param rideDTO the entity to save.
     * @return the persisted entity.
     */
    public RideDTO update(RideDTO rideDTO) {
        log.debug("Request to update Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    /**
     * Partially update a ride.
     *
     * @param rideDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RideDTO> partialUpdate(RideDTO rideDTO) {
        log.debug("Request to partially update Ride : {}", rideDTO);

        return rideRepository
            .findById(rideDTO.getId())
            .map(existingRide -> {
                rideMapper.partialUpdate(existingRide, rideDTO);

                return existingRide;
            })
            .map(rideRepository::save)
            .map(rideMapper::toDto);
    }

    /**
     * Get all the rides.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RideDTO> findAll() {
        log.debug("Request to get all Rides");
        return rideRepository.findAll().stream().map(rideMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ride by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RideDTO> findOne(Long id) {
        log.debug("Request to get Ride : {}", id);
        return rideRepository.findById(id).map(rideMapper::toDto);
    }

    /**
     * Delete the ride by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ride : {}", id);
        rideRepository.deleteById(id);
    }
}
