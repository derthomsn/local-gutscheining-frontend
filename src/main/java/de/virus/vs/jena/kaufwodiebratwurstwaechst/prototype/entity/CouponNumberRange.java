package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document("couponNumberRange")
@CompoundIndexes({
        @CompoundIndex(name = "store_running_number", def = "{'store' : 1, 'runningNumber': 1}", unique = true)
})
public class CouponNumberRange {

    private static final int COUNTER_LENGTH = 4;
    @Id
    private String id;
    
    @DBRef
    private Store store;

    @Min(1)
    private int runningNumber;
    
    @NotNull
    @NotEmpty
    private String uniquePart;
    
    private boolean used;

    public String getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getRunningNumber() {
        return runningNumber;
    }

    public void setRunningNumber(int runningNumber) {
        this.runningNumber = runningNumber;
    }

    public String getUniquePart() {
        return uniquePart;
    }

    public void setUniquePart(String uniquePart) {
        this.uniquePart = uniquePart;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public String getCouponNumber() {
        return String.format("%0" + COUNTER_LENGTH + "d", runningNumber) + "-" + uniquePart;
    }
}
