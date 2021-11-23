package com.nutrix.appointmentservice.command.api;

import com.nutrix.appointmentservice.command.application.dto.ErrorResponseDto;
import com.nutrix.appointmentservice.command.infra.Appointment;
import com.nutrix.appointmentservice.query.models.*;
import command.CreateAppointmentC;
import command.DeleteAppointmentC;
import command.UpdateAppointmentC;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import result.AppointmentUpdateResult;

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
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Appointment.class),
            @ApiResponse(code=201, message = "Appointment creado", response = Appointment.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Appointment no fue creado")
    })
    public ResponseEntity<Object> insertAppointment(@Validated @RequestBody CreateAppointmentModel appointment) {
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
                    //createAppointmentC.getId(),
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

    @PutMapping(path = "/{appointmentId}" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Modificación de un Appointment", notes ="Método que modifica un Appointment" )
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Appointment.class),
            @ApiResponse(code=201, message = "Appointment modificado", response = Appointment.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Appointment no fue modificado")
    })
    public ResponseEntity<Object> updateAppointment(@PathVariable("appointmentId") String appointmentId, @RequestBody UpdateAppointmentModel appointment){
        UpdateAppointmentC updateAppointmentC = new UpdateAppointmentC(
                appointmentId,
                appointment.getPatientId(),
                appointment.getNutritionistId(),
                appointment.getCreatedAt(),
                appointment.getLastModification(),
                appointment.getAppointmentDate(),
                appointment.getNutritionistNotes()
        );
        CompletableFuture<Object> future = commandGateway.send(updateAppointmentC);
        CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
            if (ex != null) {
                return new ErrorResponseDto(ex.getMessage());
            }
            return new AppointmentUpdateResult(
                    updateAppointmentC.getId(),
                    updateAppointmentC.getPatientId(),
                    updateAppointmentC.getNutritionistId(),
                    updateAppointmentC.getLastModification(),
                    updateAppointmentC.getAppointmentDate(),
                    updateAppointmentC.getNutritionistNotes()
            );
        });
        try {
            Object response = futureResponse.get();
            if (response instanceof AppointmentUpdateResult) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Event Sourcing Delete
    @DeleteMapping(path = "/{appointmentId}")
    @ApiOperation(value = "Eliminación de un Appointment", notes ="Método que elimina un Appointment" )
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa"),
            @ApiResponse(code=201, message = "Appointment eliminado"),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Appointment no fue eliminado")
    })
    public CompletableFuture<String> deleteAppointment(@PathVariable("appointmentId") String appointmentId){
        return commandGateway.send(new DeleteAppointmentC(appointmentId));
    }

}
