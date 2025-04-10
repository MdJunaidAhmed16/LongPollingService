package com.backend.longPolling.model;

import lombok.Data;

@Data
public class EventDTO {
    
    private String userId;
    private String eventType;
    private String message;
}
