package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto;

public class ConfirmPaymentDto {
    
    private String token;
    private String payerId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}
