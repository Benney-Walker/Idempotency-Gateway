package com.bbquantum.idempotencygateway.Service;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.DTOs.StoredInfo;
import com.bbquantum.idempotencygateway.Storage.InfoStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final InfoStorage infoStorage;

    private final long EXPIRATION_TIME = 600000;

    public PaymentService(InfoStorage infoStorage) {
        this.infoStorage = infoStorage;
    }

    public ResponseEntity<?> processPayment(String key, PaymentRequest paymentRequest) throws InterruptedException {

        String requestHash = hashRequestBody(paymentRequest);

        String storedResponse = "Charged " + paymentRequest.getAmount()
                + " " + paymentRequest.getCurrency();

        StoredInfo newInfo = new StoredInfo(
                requestHash, storedResponse, "201", true
        );

        infoStorage.setStoredInfo(key, newInfo); //Stores new transaction with the specific key

        Thread.sleep(2000);

        newInfo.setProcessing(false);

        infoStorage.setStoredInfo(key, newInfo);

        return ResponseEntity.status(201).body(storedResponse);
    }
}
