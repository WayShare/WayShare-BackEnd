package com.voituri.ridesharing.web.rest;

import com.voituri.ridesharing.repository.RideRequestRepository;
import com.voituri.ridesharing.service.RideRequestService;
import com.voituri.ridesharing.service.dto.RideRequestDTO;
import com.voituri.ridesharing.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.voituri.ridesharing.domain.RideRequest}.
 */
@RestController
@RequestMapping("/api/ride-requests")
public class RideRequestResource {

    private final Logger log = LoggerFactory.getLogger(RideRequestResource.class);

    private static final String ENTITY_NAME = "rideRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RideRequestService rideRequestService;

    private final RideRequestRepository rideRequestRepository;

    public RideRequestResource(RideRequestService rideRequestService, RideRequestRepository rideRequestRepository) {
        this.rideRequestService = rideRequestService;
        this.rideRequestRepository = rideRequestRepository;
    }

    /**
     * {@code POST  /ride-requests} : Create a new rideRequest.
     *
     * @param rideRequestDTO the rideRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rideRequestDTO, or with status {@code 400 (Bad Request)} if the rideRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RideRequestDTO> createRideRequest(@Valid @RequestBody RideRequestDTO rideRequestDTO) throws URISyntaxException {
        log.debug("REST request to save RideRequest : {}", rideRequestDTO);
        if (rideRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new rideRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rideRequestDTO = rideRequestService.save(rideRequestDTO);
        return ResponseEntity.created(new URI("/api/ride-requests/" + rideRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rideRequestDTO.getId().toString()))
            .body(rideRequestDTO);
    }

    /**
     * {@code PUT  /ride-requests/:id} : Updates an existing rideRequest.
     *
     * @param id the id of the rideRequestDTO to save.
     * @param rideRequestDTO the rideRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rideRequestDTO,
     * or with status {@code 400 (Bad Request)} if the rideRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rideRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RideRequestDTO> updateRideRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RideRequestDTO rideRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RideRequest : {}, {}", id, rideRequestDTO);
        if (rideRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rideRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rideRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rideRequestDTO = rideRequestService.update(rideRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rideRequestDTO.getId().toString()))
            .body(rideRequestDTO);
    }

    /**
     * {@code PATCH  /ride-requests/:id} : Partial updates given fields of an existing rideRequest, field will ignore if it is null
     *
     * @param id the id of the rideRequestDTO to save.
     * @param rideRequestDTO the rideRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rideRequestDTO,
     * or with status {@code 400 (Bad Request)} if the rideRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rideRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rideRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RideRequestDTO> partialUpdateRideRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RideRequestDTO rideRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RideRequest partially : {}, {}", id, rideRequestDTO);
        if (rideRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rideRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rideRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RideRequestDTO> result = rideRequestService.partialUpdate(rideRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rideRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ride-requests} : get all the rideRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rideRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RideRequestDTO>> getAllRideRequests(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RideRequests");
        Page<RideRequestDTO> page = rideRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ride-requests/:id} : get the "id" rideRequest.
     *
     * @param id the id of the rideRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rideRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RideRequestDTO> getRideRequest(@PathVariable("id") Long id) {
        log.debug("REST request to get RideRequest : {}", id);
        Optional<RideRequestDTO> rideRequestDTO = rideRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rideRequestDTO);
    }

    /**
     * {@code DELETE  /ride-requests/:id} : delete the "id" rideRequest.
     *
     * @param id the id of the rideRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRideRequest(@PathVariable("id") Long id) {
        log.debug("REST request to delete RideRequest : {}", id);
        rideRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
