package com.bbquantum.idempotencygateway.Utility;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class UtilityClass {

    public String hashRequestBody(PaymentRequest paymentRequest) {
        return paymentRequest.getAmount() + " " + paymentRequest.getCurrency();
    }
}
