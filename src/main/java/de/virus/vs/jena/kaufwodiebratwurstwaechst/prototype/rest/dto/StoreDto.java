package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Address;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;

import java.util.List;
import java.util.stream.Collectors;

public class StoreDto {
    
    private String id;
    private String name;
    private String description;
    private String email;
    private String website;
    private String logo;
    private String thumbnail;
    private String coverImage;
    private String telephone;
    private String slogan;
    private String socialLinkedin;
    private String socialFacebook;
    private String socialInstagram;
    private Address address;
    
    private List<CouponOptionDto> couponOptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CouponOptionDto> getCouponOptions() {
        return couponOptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCouponOptions(List<CouponOptionDto> couponOptions) {
        this.couponOptions = couponOptions;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSocialLinkedin() {
        return socialLinkedin;
    }

    public void setSocialLinkedin(String socialLinkedin) {
        this.socialLinkedin = socialLinkedin;
    }

    public String getSocialFacebook() {
        return socialFacebook;
    }

    public void setSocialFacebook(String socialFacebook) {
        this.socialFacebook = socialFacebook;
    }

    public String getSocialInstagram() {
        return socialInstagram;
    }

    public void setSocialInstagram(String socialInstagram) {
        this.socialInstagram = socialInstagram;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverphoto(String coverImage) {
        this.coverImage = coverImage;
    }

    public static StoreDto fromStore(Store store, boolean includeDetails) {
        StoreDto dto = new StoreDto();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setEmail(store.getEmail());
        dto.setDescription(store.getDescription());
        dto.setAddress(store.getAddress());
        dto.setWebsite(store.getWebsite());
        dto.setLogo(Base64Image.of(store.getLogo()));
        dto.setThumbnail(Base64Image.of(store.getThumbnail()));
        
        dto.setTelephone(store.getTelnumber());
        dto.setSlogan(store.getSlogan());
        dto.setSocialFacebook(store.getSocialFacebook());
        dto.setSocialInstagram(store.getSocialInstagram());
        dto.setSocialLinkedin(store.getSocialLinkedin());

        if(includeDetails) {
            dto.setCoverphoto(Base64Image.of(store.getCoverphoto()));

            if(store.getCouponTemplates() != null) {
                dto.couponOptions = store.getCouponTemplates().stream()
                        .map(CouponOptionDto::fromCouponTemplate)
                        .collect(Collectors.toList());
            }
        }
        
        return dto;
    }
    
}
