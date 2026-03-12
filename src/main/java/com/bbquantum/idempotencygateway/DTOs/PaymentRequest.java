package com.bbquantum.idempotencygateway.DTOs;

public class PaymentRequest {
    private double amount;

    private String currency;

    public PaymentRequest(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
