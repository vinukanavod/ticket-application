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

    @NonNull
    private Integer totalTickets;

    @NonNull
    private Integer ticketReleaseRate;

    @NonNull
    private Integer customerRetrievalRate;

    @NonNull
    private Integer maxTicketCapacity;


    private Vendor vendor;
}
