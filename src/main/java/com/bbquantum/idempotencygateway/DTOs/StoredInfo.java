package com.bbquantum.idempotencygateway.DTOs;

public class StoredInfo {
    private String requestHash;

    private String responseBody;

    private String statusCode;

    public StoredInfo(String requestHash, String responseBody, String statusCode) {
        this.requestHash = requestHash;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
