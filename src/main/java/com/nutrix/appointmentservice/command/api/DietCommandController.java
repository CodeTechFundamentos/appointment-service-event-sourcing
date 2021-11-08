package com.nutrix.appointmentservice.command.api;

import com.nutrix.appointmentservice.command.application.dto.ErrorResponseDto;
import com.nutrix.appointmentservice.query.models.*;
import command.CreateDietC;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/diets")
@Api(tags="Diet", value = "Servicio Web RESTFul de Diet")
public class DietCommandController {
    private final CommandGateway commandGateway;

    @Autowired
    public DietCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registro de un Diet", notes ="Método que registra un Diet" )
    @ApiResponses({
            @ApiResponse(code=201, message = "Diet creado"),
            @ApiResponse(code=404, message = "Diet no creado")
    })
    public ResponseEntity<Object> insertDiet(@Validated @RequestBody CreateDietModel diet){
        String id = UUID.randomUUID().toString();
        CreateDietC createDietC = new CreateDietC(
                id,
                diet.getName(),
                diet.getDescription(),
                diet.getCreatedAt(),
                diet.getLastModification()
        );
        CompletableFuture<Object> future = commandGateway.send(createDietC);
        CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
            if (ex != null)
                return new ErrorResponseDto(ex.getMessage());
            return new CreateDietModel(
                    createDietC.getId(),
                    createDietC.getName(),
                    createDietC.getDescription(),
                    createDietC.getCreatedAt(),
                    createDietC.getLastModification()
            );
        });

        try {
            Object response = futureResponse.get();
            if(response instanceof  CreateDietModel)
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
