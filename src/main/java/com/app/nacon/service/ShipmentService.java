package com.app.nacon.service;

import com.app.nacon.domain.Shipment;
import com.app.nacon.model.ShipmentDTO;
import com.app.nacon.repos.ShipmentRepository;
import com.app.nacon.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingService trackingService;

    public ShipmentService(final ShipmentRepository shipmentRepository, TrackingService trackingService) {
        this.shipmentRepository = shipmentRepository;
        this.trackingService = trackingService;
    }

    public List<ShipmentDTO> findAll() {
        final List<Shipment> shipments = shipmentRepository.findAll(Sort.by("id"));
        return shipments.stream()
                .map(shipment -> mapToDTO(shipment, new ShipmentDTO()))
                .toList();
    }

    public ShipmentDTO get(final Long id) {
        return shipmentRepository.findById(id)
                .map(shipment -> mapToDTO(shipment, new ShipmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public ShipmentDTO create(final ShipmentDTO shipmentDTO) {
        final Shipment shipment = new Shipment();
//        trackingService.postContainerInfo(shipmentDTO.getBillLandingNo());
        mapToEntity(shipmentDTO, shipment);
        return mapToDTO(shipmentRepository.save(shipment), new ShipmentDTO());
    }

    public void update(final Long id, final ShipmentDTO shipmentDTO) {
        final Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shipmentDTO, shipment);
        shipmentRepository.save(shipment);
    }

    public void delete(final Long id) {
        shipmentRepository.deleteById(id);
    }

    private ShipmentDTO mapToDTO(final Shipment shipment, final ShipmentDTO shipmentDTO) {
        shipmentDTO.setId(shipment.getId());
        shipmentDTO.setBillLandingNo(shipment.getBillLandingNo());
        shipmentDTO.setContainerNo(shipment.getContainerNo());
        shipmentDTO.setImporter(shipment.getImporter());
        shipmentDTO.setShippingLine(shipment.getShippingLine());
        shipmentDTO.setStatus(shipment.getStatus());
        shipmentDTO.setEta(shipment.getEta());
        shipmentDTO.setShippingReleasing(shipment.getShippingReleasing());
        shipmentDTO.setCustomDocumentation(shipment.getCustomDocumentation());
        shipmentDTO.setExaminationAndCustomReleasing(shipment.getExaminationAndCustomReleasing());
        shipmentDTO.setDelivery(shipment.getDelivery());
        shipmentDTO.setCosigneeName(shipment.getCosigneeName());
        shipmentDTO.setPortOfDischarge(shipment.getPortOfDischarge());
        return shipmentDTO;
    }

    private Shipment mapToEntity(final ShipmentDTO shipmentDTO, final Shipment shipment) {
        shipment.setBillLandingNo(shipmentDTO.getBillLandingNo());
        shipment.setContainerNo(shipmentDTO.getContainerNo());
        shipment.setImporter(shipmentDTO.getImporter());
        shipment.setShippingLine(shipmentDTO.getShippingLine());
        shipment.setStatus(shipmentDTO.getStatus());
//        shipment.setEta(shipmentDTO.getEta());
        shipment.setShippingReleasing(shipmentDTO.getShippingReleasing());
        shipment.setCustomDocumentation(shipmentDTO.getCustomDocumentation());
        shipment.setExaminationAndCustomReleasing(shipmentDTO.getExaminationAndCustomReleasing());
        shipment.setDelivery(shipmentDTO.getDelivery());
        shipment.setCosigneeName(shipmentDTO.getCosigneeName());
        shipment.setPortOfDischarge(shipmentDTO.getPortOfDischarge());
        return shipment;
    }

    public boolean billLandingNoExists(final String billLandingNo) {
        return shipmentRepository.existsByBillLandingNoIgnoreCase(billLandingNo);
    }



}
