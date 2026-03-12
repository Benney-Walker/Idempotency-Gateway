package com.bbquantum.idempotencygateway.Service;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.DTOs.StoredInfo;
import com.bbquantum.idempotencygateway.Storage.InfoStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final InfoStorage infoStorage;

    public PaymentService(InfoStorage infoStorage) {
        this.infoStorage = infoStorage;
    }

    public ResponseEntity<?> processPayment(String key, PaymentRequest paymentRequest) throws InterruptedException {
        String requestHash = hashRequestBody(paymentRequest);

        if (infoStorage.contains(key)) {
            StoredInfo existing = infoStorage.getStoredInfo(key);

            if (!existing.getRequestHash().equals(requestHash)) {
                return ResponseEntity.status(409)
                        .body("Idempotent request hash mismatch! Key already used");
            }

            return ResponseEntity.status(Integer.parseInt(existing.getStatusCode()))
                    .header("X-cache-Hit", "true")
                    .body(existing.getResponseBody());
        }

        Thread.sleep(2000);

        String storedResponse = "Charged = " + paymentRequest.getAmount()
                + " " + "Currency = " + paymentRequest.getCurrency();

        StoredInfo newInfo = new StoredInfo(
                requestHash, storedResponse, "201"
        );

        infoStorage.setStoredInfo(key, newInfo); //Stores new transaction with the specific key

        return ResponseEntity.status(201).body(storedResponse);
    }

    private String hashRequestBody(PaymentRequest paymentRequest) {
        return paymentRequest.getAmount() + "-" + paymentRequest.getCurrency();
    }
}
