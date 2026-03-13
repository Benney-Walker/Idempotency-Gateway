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

        synchronized (key.intern()) {

            String requestHash = hashRequestBody(paymentRequest);

            StoredInfo existing = infoStorage.getStoredInfo(key);

            if (existing == null) {
                String storedResponse = "Charged = " + paymentRequest.getAmount()
                        + " " + "Currency = " + paymentRequest.getCurrency();

                StoredInfo newInfo = new StoredInfo(
                        requestHash, storedResponse, "201", true
                );

                infoStorage.setStoredInfo(key, newInfo); //Stores new transaction with the specific key

                Thread.sleep(2000);

                StoredInfo processed  = infoStorage.getStoredInfo(key);
                processed.setProcessing(false);

                infoStorage.setStoredInfo(key, processed);


                return ResponseEntity.status(201).body(storedResponse);
            } else {
                long timeLeft = System.currentTimeMillis() - existing.getCreatedAt();

                if (timeLeft > EXPIRATION_TIME) {
                    infoStorage.remove(key);

                    String storedResponse = "Charged = " + paymentRequest.getAmount()
                            + " " + "Currency = " + paymentRequest.getCurrency();

                    StoredInfo newInfo = new StoredInfo(
                            requestHash, storedResponse, "201", true
                    );

                    infoStorage.setStoredInfo(key, newInfo); //Stores new transaction with the specific key

                    Thread.sleep(2000);

                    StoredInfo processed  = infoStorage.getStoredInfo(key);
                    processed.setProcessing(false);

                    infoStorage.setStoredInfo(key, processed);


                    return ResponseEntity.status(201).body(storedResponse);
                }

                if (!existing.getRequestHash().equals(requestHash)) {
                    return ResponseEntity.status(409)
                            .body("Idempotent request hash mismatch! Key already used");
                }

                while (existing.isProcessing()) {
                    Thread.sleep(1000);
                }

                return ResponseEntity.status(Integer.parseInt(existing.getStatusCode()))
                        .header("X-cache-Hit", "true")
                        .body(existing.getResponseBody());


            }
        }

    }

    private String hashRequestBody(PaymentRequest paymentRequest) {
        return paymentRequest.getAmount() + "-" + paymentRequest.getCurrency();
    }
}
