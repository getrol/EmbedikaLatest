package com.example.carservice.Entities;

public class Log {
    private String UserId = "localHost";
    private String request;
    private String requestData;

    public Log(String userId, String request, String requestData) {
        UserId = userId;
        this.request = request;
        this.requestData = requestData;
    }

    public Log(String request, String requestData) {
        this.request = request;
        this.requestData = requestData;
    }

    public Log(String request) {
        this.request = request;
        requestData = java.time.LocalDateTime.now().toString();
    }

    public String getUserId() {
        return UserId;
    }

    public String getRequest() {
        return request;
    }

    public String getRequestData() {
        return requestData;
    }

    @Override
    public String toString() {
        return "Log{" +
                "UserId='" + UserId + '\'' +
                ", request='" + request + '\'' +
                ", requestData='" + requestData + '\'' +
                '}';
    }
}
