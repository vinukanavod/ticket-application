package io.bootify.ticket_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketDTO {

    private Long id;

    @NotNull
    @TicketNumberUnique
    private Integer number;

    @JsonProperty("isSold")
    private Boolean isSold;

    private Long vendorId;

    private Long customerId;

}
