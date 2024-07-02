package com.voituri.ridesharing.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.voituri.ridesharing.domain.RideRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RideRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private String status;

    @NotNull
    private ZonedDateTime requestTime;

    private RideDTO ride;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public RideDTO getRide() {
        return ride;
    }

    public void setRide(RideDTO ride) {
        this.ride = ride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RideRequestDTO)) {
            return false;
        }

        RideRequestDTO rideRequestDTO = (RideRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rideRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RideRequestDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            ", ride=" + getRide() +
            "}";
    }
}
