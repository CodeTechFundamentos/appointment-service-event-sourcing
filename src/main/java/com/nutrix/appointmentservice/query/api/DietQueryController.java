package com.nutrix.appointmentservice.query.api;

import com.nutrix.appointmentservice.command.infra.Diet;
import com.nutrix.appointmentservice.query.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import queries.GetDietsQuery;
import result.DietResult;

import java.util.List;

@RestController
@RequestMapping("/diets")
@Api(tags="Diet", value = "Servicio de Web RESTful de Diet")
public class DietQueryController {
    private final QueryGateway queryGateway;

    @Autowired
    public DietQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Búsqueda de todos los diets", notes = "Método que busca a todos los diets")
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Diet.class),
            @ApiResponse(code=201, message = "Diet encontrados", response = Diet.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Diet no encontrados")
    })
    public ResponseEntity<List<DietResult>> getAll(){
        try {
            GetDietsQuery getDietsQuery = new GetDietsQuery();
            List<DietResult> diets = queryGateway.query(getDietsQuery,
                    ResponseTypes.multipleInstancesOf(DietResult.class))
                    .join();
            return new ResponseEntity<>(diets, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get diet by id
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Búsqueda de un diet por id", notes = "Método que busca a un diet por id")
    @ApiResponses({
            @ApiResponse(code=200, message = "La operación fue exitosa", response = Diet.class),
            @ApiResponse(code=201, message = "Diet encontrados", response = Diet.class),
            @ApiResponse(code=401, message = "Es necesario autenticar para obtener la respuesta solicitada"),
            @ApiResponse(code=403, message = "El cliente no posee los permisos necesarios"),
            @ApiResponse(code=404, message = "Diet no encontrados")
    })
    public ResponseEntity<DietResult> getById(@PathVariable("id") String id) {
        try {
            GetDietsQuery getDietsQuery = new GetDietsQuery();
            List<DietResult> diets = queryGateway.query(getDietsQuery,
                    ResponseTypes.multipleInstancesOf(DietResult.class))
                    .join();
            for (DietResult diet : diets) {
                if (diet.getId().equals(id)) {
                    return new ResponseEntity<>(diet, HttpStatus.CREATED);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
