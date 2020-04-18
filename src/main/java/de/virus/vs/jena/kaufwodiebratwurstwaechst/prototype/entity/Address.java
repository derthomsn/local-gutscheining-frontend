package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Address {

    private String name;
    
    @NotNull
    @NotEmpty
    private String street;
    
    private String houseNumber;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[0-9]{5}", message = "Invalid Zipcode")
    private String zipCode;

    public Address() {
        
    }
    
    public Address(String name, String street, String houseNumber, String city, String zipCode) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
