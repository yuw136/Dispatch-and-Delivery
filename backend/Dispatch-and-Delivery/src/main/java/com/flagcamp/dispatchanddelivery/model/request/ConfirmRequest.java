package com.flagcamp.dispatchanddelivery.model.request;

public class ConfirmRequest {
    public String messageId;
    public String orderId;
    public String action; // "PICKUP" | "DELIVERY" | "ACK"
    public String time;   // ISO string
}
