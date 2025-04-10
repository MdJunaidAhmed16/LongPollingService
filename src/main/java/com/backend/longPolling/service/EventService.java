package com.backend.longPolling.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.backend.longPolling.model.EventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventService {
    
    private StringRedisTemplate redisTemplate;

    private ObjectMapper objectMapper;

    public void processEvent(EventDTO event){
        try {
            String json = objectMapper.writeValueAsString(event);
            String key = "events:"+event.getUserId();
            redisTemplate.opsForList().leftPush(key, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
