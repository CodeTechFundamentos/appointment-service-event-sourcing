package com.nutrix.appointmentservice.command.application.handlers;

import com.nutrix.appointmentservice.command.infra.Diet;
import com.nutrix.appointmentservice.command.infra.IDietRepository;
import events.DietCreatedEvent;
import events.DietDeletedEvent;
import events.DietUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("diet")
public class DietEventHandler {
    private final IDietRepository dietRepository;

    @Autowired
    public DietEventHandler(IDietRepository dietRepository) { this.dietRepository = dietRepository; }

    @EventHandler
    public void on(DietCreatedEvent event) {
        dietRepository.save(new Diet(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCreatedAt(),
                event.getLastModification()
        ));
    }

    @EventHandler
    public void on(DietUpdatedEvent event) {
        dietRepository.save(new Diet(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCreatedAt(),
                event.getLastModification()
        ));
    }

    @EventHandler
    public void on(DietDeletedEvent event){
        dietRepository.deleteById(event.getId());
    }
}
