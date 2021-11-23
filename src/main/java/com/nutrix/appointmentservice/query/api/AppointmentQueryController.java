package com.nutrix.appointmentservice.query.api;

import com.nutrix.appointmentservice.command.infra.Appointment;
import com.nutrix.appointmentservice.query.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queries.GetAppointmentsQuery;
import result.AppointmentResult;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@Api(tags="Appointment", value = "Servicio Web RESTFul de Appointment")
public class AppointmentQueryController {
    private final QueryGateway queryGateway;

    @Autowired
    public AppointmentQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Búsqueda de todos los Appointments", notes ="Método que busca a todos los Appointments" )
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Appointment.class),
            @ApiResponse(code=201, message = "Appointments encontrados", response = Appointment.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Appointments no encontrados")
    })
    public ResponseEntity<List<AppointmentResult>> getAll(){
        try{
            GetAppointmentsQuery getAppointmentsQuery = new GetAppointmentsQuery();
            List<AppointmentResult> appointments = queryGateway.query(getAppointmentsQuery,
                    ResponseTypes.multipleInstancesOf(AppointmentResult.class))
                    .join();
            return new ResponseEntity<>(appointments, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
