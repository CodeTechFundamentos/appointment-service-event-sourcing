package com.nutrix.appointmentservice.query.api;

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
            @ApiResponse(code=201, message = "Diet encontrados"),
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
}
