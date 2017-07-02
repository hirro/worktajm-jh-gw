package com.worktajm.gw.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * In many cases an organization is only one user.
 */
@ApiModel(description = "In many cases an organization is only one user.")
@Entity
@Table(name = "domain")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "domain")
public class Domain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "domain_name", nullable = false)
    private String domainName;

    @Column(name = "organization_number")
    private String organizationNumber;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "zip_or_postcode", nullable = false)
    private String zipOrPostcode;

    @Column(name = "state_province_county")
    private String stateProvinceCounty;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "address_details")
    private String addressDetails;

    @OneToMany(mappedBy = "domain")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "domain_members",
               joinColumns = @JoinColumn(name="domains_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<User> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Domain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName() {
        return domainName;
    }

    public Domain domainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public Domain organizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
        return this;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public Domain addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public Domain addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public Domain addressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
        return this;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public Domain city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipOrPostcode() {
        return zipOrPostcode;
    }

    public Domain zipOrPostcode(String zipOrPostcode) {
        this.zipOrPostcode = zipOrPostcode;
        return this;
    }

    public void setZipOrPostcode(String zipOrPostcode) {
        this.zipOrPostcode = zipOrPostcode;
    }

    public String getStateProvinceCounty() {
        return stateProvinceCounty;
    }

    public Domain stateProvinceCounty(String stateProvinceCounty) {
        this.stateProvinceCounty = stateProvinceCounty;
        return this;
    }

    public void setStateProvinceCounty(String stateProvinceCounty) {
        this.stateProvinceCounty = stateProvinceCounty;
    }

    public String getCountry() {
        return country;
    }

    public Domain country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public Domain addressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
        return this;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Domain customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Domain addCustomers(Customer customer) {
        this.customers.add(customer);
        customer.setDomain(this);
        return this;
    }

    public Domain removeCustomers(Customer customer) {
        this.customers.remove(customer);
        customer.setDomain(null);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Domain members(Set<User> users) {
        this.members = users;
        return this;
    }

    public Domain addMembers(User user) {
        this.members.add(user);
        return this;
    }

    public Domain removeMembers(User user) {
        this.members.remove(user);
        return this;
    }

    public void setMembers(Set<User> users) {
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
        Domain domain = (Domain) o;
        if (domain.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), domain.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Domain{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", domainName='" + getDomainName() + "'" +
            ", organizationNumber='" + getOrganizationNumber() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", addressLine3='" + getAddressLine3() + "'" +
            ", city='" + getCity() + "'" +
            ", zipOrPostcode='" + getZipOrPostcode() + "'" +
            ", stateProvinceCounty='" + getStateProvinceCounty() + "'" +
            ", country='" + getCountry() + "'" +
            ", addressDetails='" + getAddressDetails() + "'" +
            "}";
    }
}
