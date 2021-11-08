package com.nutrix.appointmentservice.command.api;

import com.nutrix.appointmentservice.command.application.dto.ErrorResponseDto;
import com.nutrix.appointmentservice.query.models.*;
import command.CreateAppointmentC;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/appointments")
@Api(tags="Appointment", value = "Servicio Web RESTFul de Appointment")
public class AppointmentCommandController {
    private final CommandGateway commandGateway;

    @Autowired
    public AppointmentCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registro de un Appointment", notes ="Método que registra un Appointment" )
    @ApiResponses({
            @ApiResponse(code=201, message = "Appointment creado"),
            @ApiResponse(code=404, message = "Appointment no creado")
    })
    public ResponseEntity<Object> inserAppointment(@Validated @RequestBody CreateAppointmentModel appointment) {
        String id = UUID.randomUUID().toString();
        CreateAppointmentC createAppointmentC = new CreateAppointmentC(
                id,
                appointment.getPatientId(),
                appointment.getNutritionistId(),
                appointment.getCreatedAt(),
                appointment.getLastModification(),
                appointment.getAppointmentDate(),
                appointment.getNutritionistNotes()
        );
        CompletableFuture<Object> future = commandGateway.send(createAppointmentC);
        CompletableFuture<Object> futureResponse = future.handle((ok,ex) -> {
            if (ex != null)
                return new ErrorResponseDto(ex.getMessage());
            return new CreateAppointmentModel(
                    createAppointmentC.getId(),
                    createAppointmentC.getPatientId(),
                    createAppointmentC.getNutritionistId(),
                    createAppointmentC.getCreatedAt(),
                    createAppointmentC.getLastModification(),
                    createAppointmentC.getAppointmentDate(),
                    createAppointmentC.getNutritionistNotes()
            );
        });
        try {
            Object response = futureResponse.get();
            if (response instanceof CreateAppointmentModel) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
