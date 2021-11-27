package com.nutrix.appointmentservice.command.api;

import com.nutrix.appointmentservice.command.application.dto.ErrorResponseDto;
import com.nutrix.appointmentservice.command.infra.Appointment;
import com.nutrix.appointmentservice.query.models.*;
import command.CreateDietC;
import command.DeleteDietC;
import command.UpdateDietC;
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
import org.springframework.web.bind.annotation.*;
import result.DietUpdateResult;

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
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Appointment.class),
            @ApiResponse(code=201, message = "Diet creado", response = Appointment.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Diet no fue creado")
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

    //Event Sourcing Put
    @PutMapping(path = "/{dietId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Modificación de un Diet", notes ="Método que modifica un Diet" )
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Appointment.class),
            @ApiResponse(code=201, message = "Diet modificado", response = Appointment.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Diet no fue modificado")
    })
    public ResponseEntity<Object> updateDiet(@PathVariable("dietId") String dietId, @RequestBody UpdateDietModel diet){
        UpdateDietC updateDietC = new UpdateDietC(
                dietId,
                diet.getName(),
                diet.getDescription(),
                diet.getCreatedAt(),
                diet.getLastModification()
        );
        CompletableFuture<Object> future = commandGateway.send(updateDietC);
        CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
            if (ex != null) {
                return new ErrorResponseDto(ex.getMessage());
            }
            return new DietUpdateResult(
                    updateDietC.getId(),
                    updateDietC.getName(),
                    updateDietC.getDescription(),
                    updateDietC.getLastModification()
            );
        });
        try {
            Object response = futureResponse.get();
            if (response instanceof DietUpdateResult) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Event Sourcing Delete
    @DeleteMapping(path = "/{dietId}")
    @ApiOperation(value = "Eliminación de un Diet", notes ="Método que elimina un Diet" )
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa"),
            @ApiResponse(code=201, message = "Diet eliminado"),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Diet no fue eliminado")
    })
    public CompletableFuture<String> deleteDiet(@PathVariable("dietId") String dietId){
        return commandGateway.send(new DeleteDietC(dietId));
    }
}
