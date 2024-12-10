package io.bootify.ticket_app.service;

import io.bootify.ticket_app.domain.Customer;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.domain.Vendor;
import io.bootify.ticket_app.model.TicketDTO;
import io.bootify.ticket_app.repos.CustomerRepository;
import io.bootify.ticket_app.repos.TicketRepository;
import io.bootify.ticket_app.repos.VendorRepository;
import io.bootify.ticket_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;

    public TicketService(final TicketRepository ticketRepository,
            final VendorRepository vendorRepository, final CustomerRepository customerRepository) {
        this.ticketRepository = ticketRepository;
        this.vendorRepository = vendorRepository;
        this.customerRepository = customerRepository;
    }

    public List<TicketDTO> findAll() {
        final List<Ticket> tickets = ticketRepository.findAll(Sort.by("id"));
        return tickets.stream()
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .toList();
    }

    public TicketDTO get(final Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer TicketAdd(final Long id) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);

           ticket.setNumber(500);
           ticketRepository.save(ticket);
        return 1 ;



    }

    public Long create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);

        return ticketRepository.save(ticket).getId();
    }

    public void update(final Long id, final TicketDTO ticketDTO) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ticketDTO, ticket);
        ticketRepository.save(ticket);
    }

    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket, final TicketDTO ticketDTO) {
        ticketDTO.setId(ticket.getId());
        ticketDTO.setNumber(ticket.getNumber());

        ticketDTO.setVendorId(ticket.getVendorId() == null ? null : ticket.getVendorId().getId());
        ticketDTO.setCustomerId(ticket.getCustomerId() == null ? null : ticket.getCustomerId().getId());
        return ticketDTO;
    }

    private Ticket mapToEntity(final TicketDTO ticketDTO, final Ticket ticket) {
        ticket.setNumber(ticketDTO.getNumber());

        final Vendor vendorId = ticketDTO.getVendorId() == null ? null : vendorRepository.findById(ticketDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        ticket.setVendorId(vendorId);
        final Customer customerId = ticketDTO.getCustomerId() == null ? null : customerRepository.findById(ticketDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("customerId not found"));
        ticket.setCustomerId(customerId);
        return ticket;
    }

    public boolean numberExists(final Integer number) {
        return ticketRepository.existsByNumber(number);
    }

}
