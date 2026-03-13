package com.bbquantum.idempotencygateway.Utility;

import com.bbquantum.idempotencygateway.DTOs.PaymentRequest;
import com.bbquantum.idempotencygateway.Storage.InfoStorage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(fixedRate = 1200000)
    public void cleanExpiredKeys() {
        long now = System.currentTimeMillis();

        infoStorage.getStoredInfoList().removeIf(mappedValue ->
                now - mappedValue.getCreatedAt() > EXPIRATION_TIME);
    }
}
