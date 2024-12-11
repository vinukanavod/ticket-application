package io.bootify.ticket_app.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendorDTO {

    private Long id;

    private String name;

    private Integer totalTickets;

    private Integer ticketReleaseRate;

    private Integer maxTicketCapacity;

}
