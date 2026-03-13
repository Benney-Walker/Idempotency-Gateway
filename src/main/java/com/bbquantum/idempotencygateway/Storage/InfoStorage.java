package com.bbquantum.idempotencygateway.Storage;

import com.bbquantum.idempotencygateway.DTOs.StoredInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InfoStorage {

    private final ConcurrentHashMap<String, StoredInfo> storedInfo = new ConcurrentHashMap<>();

    public StoredInfo getStoredInfo(String key) {
        return storedInfo.get(key);
    }

    public void setStoredInfo(String key, StoredInfo storedInfo) {
        this.storedInfo.put(key, storedInfo);
    }

    public boolean contains(String key) {
        return storedInfo.containsKey(key);
    }

    public void remove(String key) {
        storedInfo.remove(key);
    }
}
