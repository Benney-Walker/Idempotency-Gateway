package com.bbquantum.idempotencygateway.Controller;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.Service.IdempotencyLayer;
import com.bbquantum.idempotencygateway.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final IdempotencyLayer idempotencyLayer;

    public PaymentController(IdempotencyLayer idempotencyLayer) {
        this.idempotencyLayer = idempotencyLayer;
    }

    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(@RequestHeader("Idempotency-Key") String key,
                                            @RequestBody PaymentRequest paymentRequest) throws InterruptedException {

        return idempotencyLayer.checkRequestValidity(key,paymentRequest);
    }

}
