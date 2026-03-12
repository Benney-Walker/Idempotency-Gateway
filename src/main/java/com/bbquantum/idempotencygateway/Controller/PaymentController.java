package com.bbquantum.idempotencygateway.Controller;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(@RequestHeader("Idempotency-Key") String key,
                                            @RequestBody PaymentRequest paymentRequest) throws InterruptedException {

        return paymentService.processPayment(key, paymentRequest);
    }

}
