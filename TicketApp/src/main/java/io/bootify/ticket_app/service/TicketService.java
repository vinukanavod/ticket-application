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
    private final VendorService vendorService;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public TicketService(final TicketRepository ticketRepository,
                         final VendorRepository vendorRepository, VendorService vendorService,
                         final CustomerRepository customerRepository, CustomerService customerService) {
        this.ticketRepository = ticketRepository;
        this.vendorRepository = vendorRepository;
        this.vendorService = vendorService;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    public List<TicketDTO> findAll() {
        final List<Ticket> tickets = ticketRepository.findAll(Sort.by("id"));
        return tickets.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TicketDTO get(final Long id) {
        return ticketRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public TicketDTO saveTicket(final TicketDTO ticketDTO) {
        Customer customer = customerRepository.findById(ticketDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customerService.purchaseTickets(customer, ticketDTO.getCount());

        Vendor vendor = vendorRepository.findById(ticketDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("Vendor not found"));

        Integer ticketsByVendor = ticketRepository.findTicketsByVendor(vendor);
        if (ticketsByVendor + ticketDTO.getCount() > vendor.getMaxTicketCapacity()) {
            throw new IllegalArgumentException("Vendor has reached max ticket capacity");
        }

        Ticket ticket = ticketRepository.findByVendorAndCustomer(vendor, customer);

        if (ticket == null) {
            ticket = new Ticket(vendor, customer, ticketDTO.getCount());
        } else {
            ticket.setCount(ticket.getCount() + ticketDTO.getCount());
        }

        ticketDTO.setId(ticketRepository.save(ticket).getId());
        ticketDTO.setCustomer(customerService.mapToCustomerDTO(customer));
        ticketDTO.setVendor(vendorService.mapToVendorDTO(vendor));
        ticketDTO.setCount(ticket.getCount());

        return ticketDTO;
    }

    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket) {
        return mapToDTO(ticket, new TicketDTO());
    }

    private TicketDTO mapToDTO(final Ticket ticket, final TicketDTO ticketDTO) {
        ticketDTO.setId(ticket.getId());
        ticketDTO.setVendorId(ticket.getVendorId() == null ? null : ticket.getVendorId().getId());
        ticketDTO.setCustomerId(ticket.getCustomerId() == null ? null : ticket.getCustomerId().getId());
        return ticketDTO;
    }

    public boolean numberExists(final Integer number) {
        return true;
    }

}
