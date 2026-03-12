package com.bbquantum.idempotencygateway.DTOs;

public class StoredInfo {
    private String requestHash;

    private String responseBody;

    private String statusCode;

    private boolean processing;

    public StoredInfo(String requestHash, String responseBody, String statusCode, boolean processing) {
        this.requestHash = requestHash;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.processing = processing;
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

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
