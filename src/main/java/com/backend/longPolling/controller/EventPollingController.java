package com.backend.longPolling.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.backend.longPolling.service.EventQueueService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPollingController {
    
    private final EventQueueService eventQueueService;

    @GetMapping("/{userId}")
    public DeferredResult<ResponseEntity<String>> pollEvents(@PathVariable String userId){
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();

        String event = eventQueueService.getEventForUser(userId);

        if(event != null){
            deferredResult.setResult(ResponseEntity.ok(event));
        }
        else{
            eventQueueService.registerWaitingUser(userId, deferredResult);
        }

        deferredResult.onTimeout(() -> deferredResult.setResult(ResponseEntity.noContent().build()));

        return deferredResult;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> pushEvent(@PathVariable String userId, @RequestBody String eventPayload){
        eventQueueService.pushEventToUser(userId, eventPayload);

        return ResponseEntity.ok("Event pushed to "+userId);
    }



}
