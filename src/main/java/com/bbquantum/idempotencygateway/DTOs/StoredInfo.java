package com.bbquantum.idempotencygateway.DTOs;

public class StoredInfo {
    private String requestBody;

    private String responseBody;

    private String statusCode;

    public StoredInfo(String requestBody, String responseBody, String statusCode) {
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
