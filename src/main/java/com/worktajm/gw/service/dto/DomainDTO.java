package com.worktajm.gw.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Domain entity.
 */
public class DomainDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String domainName;

    private String organizationNumber;

    private Long addressId;

    private Set<UserDTO> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Set<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<UserDTO> users) {
        this.members = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DomainDTO domainDTO = (DomainDTO) o;
        if(domainDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), domainDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DomainDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", domainName='" + getDomainName() + "'" +
            ", organizationNumber='" + getOrganizationNumber() + "'" +
            "}";
    }
}
