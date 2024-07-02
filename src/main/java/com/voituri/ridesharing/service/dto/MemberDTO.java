package com.voituri.ridesharing.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.voituri.ridesharing.domain.Member} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String login;

    @NotNull
    private String passwordHash;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private Boolean activated;

    private ProfileDTO profile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", email='" + getEmail() + "'" +
            ", activated='" + getActivated() + "'" +
            ", profile=" + getProfile() +
            "}";
    }
}
