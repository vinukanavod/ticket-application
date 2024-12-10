package io.bootify.ticket_app.model;

import io.bootify.ticket_app.domain.Vendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class SystemDTO {


    private Long id;


    private Integer totalTickets;


    private Integer ticketReleaseRate;


//    private Integer customerRetrievalRate;


//    private Integer maxTicketCapacity;


    private Vendor vendor;
}
