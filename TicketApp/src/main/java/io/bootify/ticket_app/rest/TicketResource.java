package io.bootify.ticket_app.rest;

import io.bootify.ticket_app.model.TicketDTO;
import io.bootify.ticket_app.service.TicketService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketResource {

    private final TicketService ticketService;

    public TicketResource(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ticketService.get(id));
    }

    @PostMapping("/save")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<TicketDTO> saveTicket(@RequestBody @Valid final TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.saveTicket(ticketDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTicket(@PathVariable(name = "id") final Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
