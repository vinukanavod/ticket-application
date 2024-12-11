package io.bootify.ticket_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {

    private Long id;

    private String name;

    private Integer totalTickets;

    private Integer customerRetrievalRate;

    private Integer maxTicketCapacity;

}
