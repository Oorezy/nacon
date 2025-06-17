package com.app.nacon.service;

import com.app.nacon.domain.Shipment;
import com.app.nacon.model.ShipmentDTO;
import com.app.nacon.model.TrackingResponse;
import com.app.nacon.repos.ShipmentRepository;
import com.app.nacon.util.NotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingService trackingService;

    public ShipmentService(final ShipmentRepository shipmentRepository, TrackingService trackingService) {
        this.shipmentRepository = shipmentRepository;
        this.trackingService = trackingService;
    }

    @Scheduled(fixedRate = 21600000)
    private void refresh(){
        List<Shipment> shipments = shipmentRepository.findAll();
        List<Shipment> updatedShipments = new ArrayList<>();

        for (Shipment shipment : shipments) {
            try {
                TrackingResponse response = trackingService.getETA(shipment.getBillLandingNo());

                if (response != null) {
                    shipment.setEta(LocalDate.parse(response.getArrival().getEta()));
                    shipment.setStatus(response.getStatus());
                    shipment.setVessel(response.getVessel());
                    updatedShipments.add(shipment);
                }
            } catch (Exception e) {
                System.err.println("Error updating ETA for B/L No: " + shipment.getBillLandingNo());
                e.printStackTrace();
            }
            System.out.println("\nRefreshed Shipment::");
        }
        shipmentRepository.saveAll(updatedShipments);
    }

    public List<ShipmentDTO> findAll() {
        final List<Shipment> shipments = shipmentRepository.findAllByOrderByEtaAsc();
        return shipments.stream()
                .map(shipment -> mapToDTO(shipment, new ShipmentDTO()))
                .toList();
    }

    public ShipmentDTO get(final Long id) {
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment!=null){
//            var eta = trackingService.getETA(shipment.getBillLandingNo());
//            shipment.setEta(eta);
            return mapToDTO(shipment, new ShipmentDTO());
        }
        return null;
    }

    public ShipmentDTO create(final ShipmentDTO shipmentDTO) {
        final Shipment shipment = new Shipment();
        mapToEntity(shipmentDTO, shipment);
        Shipment entity = shipmentRepository.save(shipment);
        getApiDetails(entity);
        return mapToDTO(entity, new ShipmentDTO());
    }

    @Async
    public void getApiDetails(Shipment shipment) {
        trackingService.postContainerInfo(shipment.getBillLandingNo())
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(response -> {

                    TrackingResponse trackingResponse = trackingService.getETA(shipment.getBillLandingNo());
                    shipment.setEta(LocalDate.parse(trackingResponse.getArrival().getEta()));
                    shipment.setStatus(trackingResponse.getStatus());
                    shipment.setVessel(trackingResponse.getVessel());
                    shipmentRepository.save(shipment);
                    System.out.println("ETA result: " + trackingResponse);
                })
                .doOnError(error -> {
                    System.err.println("Error: " + error.getMessage());
                })
                .subscribe();
        System.out.println("\n\nDone getApiDetails");
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
        shipmentDTO.setVessel(shipment.getVessel());
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
//        shipment.setStatus(shipmentDTO.getStatus());
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
