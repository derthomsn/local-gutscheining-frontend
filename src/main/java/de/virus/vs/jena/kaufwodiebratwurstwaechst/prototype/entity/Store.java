package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import org.bson.BsonBinarySubType;
import org.bson.internal.Base64;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document("store")
public class Store {
	
    @Id
    private String id;

    @NotNull
    @Size(min = 2, max = 75)
    private String name;
    
    @NotNull
    @NotEmpty(message = "Enter a description")
    private String slogan;

    @NotNull
    @NotEmpty(message = "Enter a description")
    private String description;
    
    @Pattern(regexp = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$", message = "Invalid phone number?")
    private String telnumber;
    
    private String socialLinkedin;
    
    private String socialFacebook;
    
    private String socialInstagram;    

    @Email(message = "Invalid e-mail address")
    @Indexed(name = "idx-store-email")
    private String email;
    
    @NotNull
    @NotEmpty
    @Indexed(name = "idx-store-email-internal", unique = true)
    private String emailinternal;

    @Valid
    @NotNull
    public Address address;

    private String website;

    @Valid
    @NotNull
    private PaypalInformation paypalInformation;

    private List<CouponTemplate> couponTemplates = new ArrayList<>();

    private Binary logo;
    
    private Binary coverphoto;
    
    private Binary thumbnail;

    public Store() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailinternal() {
		return emailinternal;
	}

	public void setEmailinternal(String emailinternal) {
		this.emailinternal = emailinternal;
	}

	public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public PaypalInformation getPaypalInformation() {
        return paypalInformation;
    }

    public void setPaypalInformation(PaypalInformation paypalInformation) {
        this.paypalInformation = paypalInformation;
    }

    public Binary getLogo() {
        return logo;
    }
    
    public String getLogoBinaryAsString() {
        if(getLogo() == null) {
            return null;
        }
        return Base64.encode(this.getLogo().getData());
    }

    public void setLogo(MultipartFile logo) throws IllegalStateException{
    	try {
    		if(logo.getSize() >= 524288L) {
    			System.out.println("triggered");
    			throw new IllegalStateException("Logo is bigger than 524.288 bytes");
    		}
			this.logo = new Binary(BsonBinarySubType.BINARY, logo.getBytes());
		} catch (IOException e) { 
    		e.printStackTrace();
		}
    }
    
    public void setLogoBinary(Binary logo) {
    	this.logo = logo;
    }

    public Binary getCoverphoto() {
		return coverphoto;
	}
    
    public String getCoverphotoBinaryAsString() {
        return Base64.encode(this.getCoverphoto().getData());
    }
    
	public void setCoverphoto(MultipartFile coverphoto) {
		try {
    		if(coverphoto.getSize() >= 2000000) {
    			throw new IllegalStateException("Logo is bigger than 2 MB");
    		}
			this.coverphoto = new Binary(BsonBinarySubType.BINARY, coverphoto.getBytes());
		} catch (IOException e) { 
    		e.printStackTrace();
		}
	}
	
	public void setCoverphotoBinary(Binary coverphoto) {
    	this.coverphoto = coverphoto;
    }

	public Binary getThumbnail() {
		return thumbnail;
	}
	
	public String getThumbnailBinaryAsString() {
        return Base64.encode(this.getThumbnail().getData());
    }

	public void setThumbnail(MultipartFile thumbnail) {
		try {
    		if(thumbnail.getSize() >= 524288L) {
    			throw new IllegalStateException("Logo is bigger than 524.288 bytes");
    		}
			this.thumbnail = new Binary(BsonBinarySubType.BINARY, thumbnail.getBytes());
		} catch (IOException e) { 
    		e.printStackTrace();
		}
	}
	
	public void setThumbnailBinary(Binary thumbnail) {
    	this.thumbnail = thumbnail;
    }

    public List<CouponTemplate> getCouponTemplates() {
        return couponTemplates;
    }
 
    public void addCouponTemplate(CouponTemplate couponTemplate) {
        couponTemplates.add(couponTemplate);
    }

    public void removeCouponTemplate(CouponTemplate couponTemplate) {
        couponTemplates.remove(couponTemplate);
    }

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getTelnumber() {
		return telnumber;
	}

	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}

	public String getSocialLinkedin() {
		return socialLinkedin;
	}

	public void setSocialTwitter(String socialLinkedin) {
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

    public CouponTemplate getCouponTemplateById(String couponTemplateId) {
        return getCouponTemplates().stream().filter(ct -> Objects.equals(ct.getId(), couponTemplateId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find template " + couponTemplateId + " in store " + id));
    }
}
