package com.app.nacon.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShipmentDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @ShipmentBillLandingNoUnique
    private String billLandingNo;

    private List<@Size(max = 255) String> containerNo;

    @Size(max = 255)
    private String importer;

    private ShippingLine shippingLine;

    private String status;

    @Size(max = 255)
    private LocalDate eta;

    @Size(max = 255)
    private String shippingReleasing;

    private String customDocumentation;

    @Size(max = 255)
    private String examinationAndCustomReleasing;

    @Size(max = 255)
    private String delivery;

    @Size(max = 255)
    private String cosigneeName;

    @Size(max = 255)
    private String portOfDischarge;

    private String vessel;


}
