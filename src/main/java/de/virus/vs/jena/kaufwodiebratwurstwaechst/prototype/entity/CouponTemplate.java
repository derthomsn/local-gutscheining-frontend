package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import java.io.IOException;
import java.math.BigDecimal;

import org.bson.BsonBinarySubType;
import org.bson.internal.Base64;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

public class CouponTemplate {
    
    @Id
    public ObjectId id;

    public String name;
    
    private String description;
    
    private BigDecimal price;
    
    private BigDecimal value;
    
    private BigDecimal taxes;

    private Binary couponImage;
    
    private Binary couponImageThumbnail;

    public CouponTemplate() {
    	this.id = ObjectId.get();
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

	public Binary getCouponImage() {
        return couponImage;
    }
	
	public String getCouponImageBinaryAsString() {
        return Base64.encode(this.getCouponImage().getData());
    }

    public void setCouponImage(MultipartFile couponImage) throws IllegalStateException{
        try {
        	if(couponImage.getSize() >= 524288L) {
    			throw new IllegalStateException();
    		}
			this.couponImage = new Binary(BsonBinarySubType.BINARY, couponImage.getBytes());
		} catch (IOException e) { 
    		e.printStackTrace();
		}
    }
    
    public void setCouponImageBinary(Binary couponImage) {
        this.couponImage = couponImage;
    }
    
    public Binary getCouponImageThumbnail() {
        return this.couponImageThumbnail;
    }
    
    public String getCouponImageThumbnailBinaryAsString() {
    	if(getCouponImageThumbnail() == null) {
    		return null;
		}
        return Base64.encode(this.getCouponImageThumbnail().getData());
    }
    
    public void setCouponImageThumbnailBinary(Binary couponImageThumbnail) {
        this.couponImageThumbnail = couponImageThumbnail;
    }
    
    public void setCouponImageThumbnail(MultipartFile couponImageThumbnail) throws IllegalStateException{
        try {
        	if(couponImageThumbnail.getSize() >= 524288L) {
    			throw new IllegalStateException();
    		}
			this.couponImageThumbnail = new Binary(BsonBinarySubType.BINARY, couponImageThumbnail.getBytes());
		} catch (IOException e) { 
    		e.printStackTrace();
		}
    }

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getId() {
		return this.id.toString();
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}
}
