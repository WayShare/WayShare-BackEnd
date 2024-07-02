package com.voituri.ridesharing.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.voituri.ridesharing.domain.Ride} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RideDTO implements Serializable {

    private Long id;

    @NotNull
    private String startLocation;

    @NotNull
    private String endLocation;

    @NotNull
    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private Boolean recurring;

    private MemberDTO member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RideDTO)) {
            return false;
        }

        RideDTO rideDTO = (RideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RideDTO{" +
            "id=" + getId() +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", recurring='" + getRecurring() + "'" +
            ", member=" + getMember() +
            "}";
    }
}
