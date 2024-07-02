package com.voituri.ridesharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotNull
    @Column(name = "end_location", nullable = false)
    private String endLocation;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "recurring")
    private Boolean recurring;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ride")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ride" }, allowSetters = true)
    private Set<RideRequest> requests = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ride")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ride" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "profile", "rides", "notifications", "ratingsGivens", "ratingsReceiveds" }, allowSetters = true)
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ride id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public Ride startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return this.endLocation;
    }

    public Ride endLocation(String endLocation) {
        this.setEndLocation(endLocation);
        return this;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Ride startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public Ride endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getRecurring() {
        return this.recurring;
    }

    public Ride recurring(Boolean recurring) {
        this.setRecurring(recurring);
        return this;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Set<RideRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<RideRequest> rideRequests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setRide(null));
        }
        if (rideRequests != null) {
            rideRequests.forEach(i -> i.setRide(this));
        }
        this.requests = rideRequests;
    }

    public Ride requests(Set<RideRequest> rideRequests) {
        this.setRequests(rideRequests);
        return this;
    }

    public Ride addRequests(RideRequest rideRequest) {
        this.requests.add(rideRequest);
        rideRequest.setRide(this);
        return this;
    }

    public Ride removeRequests(RideRequest rideRequest) {
        this.requests.remove(rideRequest);
        rideRequest.setRide(null);
        return this;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setRide(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setRide(this));
        }
        this.messages = messages;
    }

    public Ride messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Ride addMessages(Message message) {
        this.messages.add(message);
        message.setRide(this);
        return this;
    }

    public Ride removeMessages(Message message) {
        this.messages.remove(message);
        message.setRide(null);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Ride member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ride)) {
            return false;
        }
        return getId() != null && getId().equals(((Ride) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ride{" +
            "id=" + getId() +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", recurring='" + getRecurring() + "'" +
            "}";
    }
}
