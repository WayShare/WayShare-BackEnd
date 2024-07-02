package com.voituri.ridesharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "activated")
    private Boolean activated;

    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requests", "messages", "member" }, allowSetters = true)
    private Set<Ride> rides = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "giver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "giver", "receiver" }, allowSetters = true)
    private Set<Rating> ratingsGivens = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "giver", "receiver" }, allowSetters = true)
    private Set<Rating> ratingsReceiveds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Member login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public Member passwordHash(String passwordHash) {
        this.setPasswordHash(passwordHash);
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return this.email;
    }

    public Member email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Member activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Member profile(Profile profile) {
        this.setProfile(profile);
        return this;
    }

    public Set<Ride> getRides() {
        return this.rides;
    }

    public void setRides(Set<Ride> rides) {
        if (this.rides != null) {
            this.rides.forEach(i -> i.setMember(null));
        }
        if (rides != null) {
            rides.forEach(i -> i.setMember(this));
        }
        this.rides = rides;
    }

    public Member rides(Set<Ride> rides) {
        this.setRides(rides);
        return this;
    }

    public Member addRides(Ride ride) {
        this.rides.add(ride);
        ride.setMember(this);
        return this;
    }

    public Member removeRides(Ride ride) {
        this.rides.remove(ride);
        ride.setMember(null);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setMember(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setMember(this));
        }
        this.notifications = notifications;
    }

    public Member notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public Member addNotifications(Notification notification) {
        this.notifications.add(notification);
        notification.setMember(this);
        return this;
    }

    public Member removeNotifications(Notification notification) {
        this.notifications.remove(notification);
        notification.setMember(null);
        return this;
    }

    public Set<Rating> getRatingsGivens() {
        return this.ratingsGivens;
    }

    public void setRatingsGivens(Set<Rating> ratings) {
        if (this.ratingsGivens != null) {
            this.ratingsGivens.forEach(i -> i.setGiver(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setGiver(this));
        }
        this.ratingsGivens = ratings;
    }

    public Member ratingsGivens(Set<Rating> ratings) {
        this.setRatingsGivens(ratings);
        return this;
    }

    public Member addRatingsGiven(Rating rating) {
        this.ratingsGivens.add(rating);
        rating.setGiver(this);
        return this;
    }

    public Member removeRatingsGiven(Rating rating) {
        this.ratingsGivens.remove(rating);
        rating.setGiver(null);
        return this;
    }

    public Set<Rating> getRatingsReceiveds() {
        return this.ratingsReceiveds;
    }

    public void setRatingsReceiveds(Set<Rating> ratings) {
        if (this.ratingsReceiveds != null) {
            this.ratingsReceiveds.forEach(i -> i.setReceiver(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setReceiver(this));
        }
        this.ratingsReceiveds = ratings;
    }

    public Member ratingsReceiveds(Set<Rating> ratings) {
        this.setRatingsReceiveds(ratings);
        return this;
    }

    public Member addRatingsReceived(Rating rating) {
        this.ratingsReceiveds.add(rating);
        rating.setReceiver(this);
        return this;
    }

    public Member removeRatingsReceived(Rating rating) {
        this.ratingsReceiveds.remove(rating);
        rating.setReceiver(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return getId() != null && getId().equals(((Member) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", email='" + getEmail() + "'" +
            ", activated='" + getActivated() + "'" +
            "}";
    }
}
