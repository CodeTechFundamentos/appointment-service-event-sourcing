package com.nutrix.appointmentservice.query.application.handlers;

import com.nutrix.appointmentservice.command.infra.Diet;
import com.nutrix.appointmentservice.command.infra.IDietRepository;
import com.nutrix.appointmentservice.query.models.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import queries.GetDietsQuery;
import result.DietResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DietQueryHandler {
    private final IDietRepository dietRepository;

    @Autowired
    public DietQueryHandler(IDietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    @QueryHandler
    public List<DietResult> handle(GetDietsQuery query) {
        List<Diet> diets = dietRepository.findAll();

        List<DietResult> dietModels =
                diets.stream()
                        .map(diet -> new DietResult(
                                diet.getId(),
                                diet.getName(),
                                diet.getDescription(),
                                diet.getCreatedAt(),
                                diet.getLastModification()
                        )).collect(Collectors.toList());
        return dietModels;
    }
}
