package com.nutrix.appointmentservice.config;

import com.nutrix.appointmentservice.command.domain.AppointmentA;
import com.nutrix.appointmentservice.command.domain.DietA;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public Repository<DietA> eventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(DietA.class)
                .eventStore(eventStore)
                .build();
    }

    @Bean
    public Repository<AppointmentA> eventSourcingRepository2(EventStore eventStore) {
        return EventSourcingRepository.builder(AppointmentA.class)
                .eventStore(eventStore)
                .build();
    }
}

