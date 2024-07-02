package com.voituri.ridesharing.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.voituri.ridesharing.domain.Profile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String photo;

    private String contactDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", contactDetails='" + getContactDetails() + "'" +
            "}";
    }
}
