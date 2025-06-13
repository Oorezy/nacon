package com.app.nacon.rest;

import com.app.nacon.model.ShipmentDTO;
import com.app.nacon.service.ShipmentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RestController
@RequestMapping(value = "/api/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShipmentResource {

    private final ShipmentService shipmentService;

    public ShipmentResource(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getShipment(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(shipmentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody @Valid final ShipmentDTO shipmentDTO) {
//        final Long createdId = shipmentService.create(shipmentDTO);
        return new ResponseEntity<>(shipmentService.create(shipmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateShipment(@RequestBody @Valid final ShipmentDTO shipmentDTO) {
        Long id = shipmentDTO.getId();
        shipmentService.update(id, shipmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteShipment(@PathVariable(name = "id") final Long id) {
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
