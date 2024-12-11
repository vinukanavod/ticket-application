package io.bootify.ticket_app.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketDTO {

    private Long id;

    private Long vendorId;

    private VendorDTO vendor;

    private Long customerId;

    private CustomerDTO customer;

    private Integer count;

}
