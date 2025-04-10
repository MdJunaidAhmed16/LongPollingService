package com.backend.longPolling.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.http.ResponseEntity;

@Service
public class EventQueueService {
    
    private final RedisTemplate<String, String> redisTemplate;

    public EventQueueService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final Map<String, DeferredResult<ResponseEntity<String>>> waitingUsers = new ConcurrentHashMap<>();


    public String getEventForUser(String userId){
        String key = "iam:events:"+userId;
        return redisTemplate.opsForList().leftPop(key);
    }

    public void registerWaitingUser(String userId, DeferredResult<ResponseEntity<String>> deferredResult){
        waitingUsers.put(userId, deferredResult);
    }

    public void pushEventToUser(String userId, String event){
        DeferredResult<ResponseEntity<String>> waitingClient = waitingUsers.remove(userId);

        if(waitingClient != null){
            waitingClient.setResult(ResponseEntity.ok(event));
        }
        else{
            String key = "iam:events:"+userId;
            redisTemplate.opsForList().rightPush(key, event);
        }
    }

    
}
