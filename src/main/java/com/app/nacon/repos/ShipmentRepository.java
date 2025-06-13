package com.app.nacon.repos;

import com.app.nacon.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    boolean existsByBillLandingNoIgnoreCase(String billLandingNo);

}
