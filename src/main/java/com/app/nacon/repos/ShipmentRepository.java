package com.app.nacon.repos;

import com.app.nacon.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    boolean existsByBillLandingNoIgnoreCase(String billLandingNo);

    List<Shipment> findAllByOrderByEtaAsc();
}
