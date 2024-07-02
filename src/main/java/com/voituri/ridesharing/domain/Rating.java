package com.voituri.ridesharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "profile", "rides", "notifications", "ratingsGivens", "ratingsReceiveds" }, allowSetters = true)
    private Member giver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "profile", "rides", "notifications", "ratingsGivens", "ratingsReceiveds" }, allowSetters = true)
    private Member receiver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return this.score;
    }

    public Rating score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public Rating feedback(String feedback) {
        this.setFeedback(feedback);
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Member getGiver() {
        return this.giver;
    }

    public void setGiver(Member member) {
        this.giver = member;
    }

    public Rating giver(Member member) {
        this.setGiver(member);
        return this;
    }

    public Member getReceiver() {
        return this.receiver;
    }

    public void setReceiver(Member member) {
        this.receiver = member;
    }

    public Rating receiver(Member member) {
        this.setReceiver(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return getId() != null && getId().equals(((Rating) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", feedback='" + getFeedback() + "'" +
            "}";
    }
}
