package com.voituri.ridesharing.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.voituri.ridesharing.domain.Rating} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RatingDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer score;

    private String feedback;

    private MemberDTO giver;

    private MemberDTO receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public MemberDTO getGiver() {
        return giver;
    }

    public void setGiver(MemberDTO giver) {
        this.giver = giver;
    }

    public MemberDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(MemberDTO receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatingDTO)) {
            return false;
        }

        RatingDTO ratingDTO = (RatingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ratingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RatingDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", feedback='" + getFeedback() + "'" +
            ", giver=" + getGiver() +
            ", receiver=" + getReceiver() +
            "}";
    }
}
