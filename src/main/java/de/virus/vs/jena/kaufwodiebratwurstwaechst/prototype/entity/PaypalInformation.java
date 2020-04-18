package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PaypalInformation {
    
    @NotNull
    @NotEmpty
    private String clientId;
    
    @NotNull
    @NotEmpty
    private String clientSecret;
    
    private boolean sandBoxAccount;

    public PaypalInformation() {
        
    }
    
    public PaypalInformation(String clientId, String clientSecret, boolean sandBoxAccount) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.sandBoxAccount = sandBoxAccount;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public boolean isSandBoxAccount() {
        return sandBoxAccount;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setSandBoxAccount(boolean sandBoxAccount) {
        this.sandBoxAccount = sandBoxAccount;
    }
}
