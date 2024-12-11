package io.bootify.ticket_app.service;

import io.bootify.ticket_app.domain.Customer;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.model.CustomerDTO;
import io.bootify.ticket_app.repos.CustomerRepository;
import io.bootify.ticket_app.repos.TicketRepository;
import io.bootify.ticket_app.util.NotFoundException;
import io.bootify.ticket_app.util.ReferencedWarning;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    public CustomerService(CustomerRepository customerRepository, TicketRepository ticketRepository) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(this::mapToCustomerDTO)
                .toList();
    }

    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(this::mapToCustomerDTO)
                .orElseThrow(NotFoundException::new);
    }

    public CustomerDTO create(final CustomerDTO customerDTO) {
        if (customerDTO.getName() == null) {
            throw new IllegalArgumentException("Customer name is required");
        }
        final Customer customer = customerRepository.save(new Customer(customerDTO.getName()));
        return mapToCustomerDTO(customer);
    }

    @Transactional
    public CustomerDTO update(final Long id, final CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        customer.setMaxTicketCapacity(customerDTO.getMaxTicketCapacity() != null ? customerDTO.getMaxTicketCapacity() : customer.getMaxTicketCapacity());
        customer.setCustomerRetrievalRate(customerDTO.getCustomerRetrievalRate() != null ? customerDTO.getCustomerRetrievalRate() : customer.getCustomerRetrievalRate());
        customerRepository.save(customer);

        return mapToCustomerDTO(customer);
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    public void purchaseTickets(Customer customer, final Integer count) {
        if (customer.getTotalTickets() == null) {
            customer.setTotalTickets(0);
        } else if (customer.getTotalTickets() + count > customer.getMaxTicketCapacity()) {
            throw new IllegalArgumentException("Customer has reached the maximum ticket capacity");
        }
        customer.setTotalTickets(customer.getTotalTickets() + count);
        customerRepository.save(customer);
    }

    public CustomerDTO mapToCustomerDTO(final Customer customer) {
        return mapToCustomerDTO(customer, new CustomerDTO());
    }

    public CustomerDTO mapToCustomerDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setTotalTickets(customer.getTotalTickets());
        customerDTO.setCustomerRetrievalRate(customer.getCustomerRetrievalRate());
        customerDTO.setMaxTicketCapacity(customer.getMaxTicketCapacity());
        return customerDTO;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket customerIdTicket = ticketRepository.findFirstByCustomerId(customer);
        if (customerIdTicket != null) {
            referencedWarning.setKey("customer.ticket.customerId.referenced");
            referencedWarning.addParam(customerIdTicket.getId());
            return referencedWarning;
        }
        return null;
    }

}
