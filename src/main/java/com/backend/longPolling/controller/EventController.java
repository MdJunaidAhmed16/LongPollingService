package com.backend.longPolling.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.longPolling.model.EventDTO;
import com.backend.longPolling.service.EventService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/emit-event")
@AllArgsConstructor
public class EventController {
    
    private EventService eventService;

    public void emitEvent(@RequestBody EventDTO event){
        eventService.processEvent(event);
    }
}
