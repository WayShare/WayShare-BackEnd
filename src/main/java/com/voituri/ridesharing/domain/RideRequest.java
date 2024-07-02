package com.voituri.ridesharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RideRequest.
 */
@Entity
@Table(name = "ride_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RideRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "request_time", nullable = false)
    private ZonedDateTime requestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "requests", "messages", "member" }, allowSetters = true)
    private Ride ride;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RideRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public RideRequest status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getRequestTime() {
        return this.requestTime;
    }

    public RideRequest requestTime(ZonedDateTime requestTime) {
        this.setRequestTime(requestTime);
        return this;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Ride getRide() {
        return this.ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public RideRequest ride(Ride ride) {
        this.setRide(ride);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RideRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((RideRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RideRequest{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            "}";
    }
}
