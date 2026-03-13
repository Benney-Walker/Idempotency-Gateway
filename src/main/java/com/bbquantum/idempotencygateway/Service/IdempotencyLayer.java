package com.bbquantum.idempotencygateway.Service;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.DTOs.StoredInfo;
import com.bbquantum.idempotencygateway.Storage.InfoStorage;
import com.bbquantum.idempotencygateway.Utility.UtilityClass;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyLayer {

    private final PaymentService paymentService;

    private final InfoStorage infoStorage;

    private final UtilityClass utilityClass;

    public IdempotencyLayer(PaymentService paymentService, InfoStorage infoStorage, UtilityClass utilityClass) {
        this.paymentService = paymentService;
        this.infoStorage = infoStorage;
        this.utilityClass = utilityClass;
    }

    public ResponseEntity<?> checkRequestValidity(String key, PaymentRequest paymentRequest) throws InterruptedException {
        synchronized (key.intern()) {
            if (!checkKey(key)) {
                paymentService.processPayment(key, paymentRequest);
            }

            if (!checkRequestHash(key, paymentRequest)) {
                return ResponseEntity.status(409)
                        .body("Idempotent request hash mismatch! Key already used");
            }

            if (!checkProcessingState(key)) {
                Thread.sleep(1000);
            }

            StoredInfo storedInfo = infoStorage.getStoredInfo(key);
            return ResponseEntity.status(201)
                    .body(storedInfo.getResponseBody());
        }
    }

    private boolean checkKey(String key) {
        StoredInfo storedInfo = infoStorage.getStoredInfo(key);
        return storedInfo != null;
    }

    private boolean checkRequestHash(String key, PaymentRequest paymentRequest) throws InterruptedException {
        StoredInfo storedInfo = infoStorage.getStoredInfo(key);

        String newRequestHash = utilityClass.hashRequestBody(paymentRequest);

        return storedInfo.getRequestHash().equals(newRequestHash);
    }

    private boolean checkProcessingState(String key) {
        StoredInfo storedInfo = infoStorage.getStoredInfo(key);
        return storedInfo.isProcessing();
    }
}
