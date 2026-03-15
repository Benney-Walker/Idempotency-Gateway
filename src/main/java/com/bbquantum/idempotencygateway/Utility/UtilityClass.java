package com.bbquantum.idempotencygateway.Utility;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.DTOs.StoredInfo;
import com.bbquantum.idempotencygateway.Storage.InfoStorage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtilityClass {

    private final InfoStorage infoStorage;

    private final long EXPIRATION_TIME = 3600000;

    public UtilityClass(InfoStorage infoStorage) {
        this.infoStorage = infoStorage;
    }

    public String hashRequestBody(PaymentRequest paymentRequest) {
        return paymentRequest.getAmount() + " " + paymentRequest.getCurrency();
    }

    @Scheduled(fixedRate = 1200000) // Runs every 20 minutes
    public void cleanExpiredKeys() {

        long now = System.currentTimeMillis();

        infoStorage.getStorageMap().entrySet().removeIf(entry ->
                now - entry.getValue().getCreatedAt() > EXPIRATION_TIME
        );
    }
}
