package io.bootify.ticket_app.service;

import io.bootify.ticket_app.domain.CustomConfig;
import io.bootify.ticket_app.domain.Customer;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.model.CustomerConfigDTO;
import io.bootify.ticket_app.model.CustomerDTO;
import io.bootify.ticket_app.repos.CustomerConfigRepository;
import io.bootify.ticket_app.repos.CustomerRepository;
import io.bootify.ticket_app.repos.TicketRepository;
import io.bootify.ticket_app.util.NotFoundException;
import io.bootify.ticket_app.util.ReferencedWarning;
import java.util.List;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    private final CustomerConfigRepository customerConfigRepository;

    public CustomerService(CustomerRepository customerRepository, TicketRepository ticketRepository, CustomerConfigRepository customerConfigRepository) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
        this.customerConfigRepository = customerConfigRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public CustomerDTO create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);

        return mapToDTO(customerRepository.save(customer) , new CustomerDTO());
    }
   @Transactional
    public CustomerDTO  update(final Long id, final CustomerConfigDTO customerConfigDTO) {
         Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);

         CustomConfig customConfig = new CustomConfig();
         customConfig.setCustomerRetrievalRate(customerConfigDTO.getCustomerRetrievalRate());
         customConfig.setMaxTicketCapacity(customerConfigDTO.getMaxTicketCapacity());
         customConfig.setCustomer( customer);
         CustomConfig   customConfig1 =  customerConfigRepository.save(customConfig);

         //CustomerDTO customerDTO =  mapToDTO(customerRepository.getById(id) , new CustomerDTO());
//        System.out.println("vinuka navod " + customerDTO .getName());
        Customer customer1 =  customerRepository.getById(id);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer1.getId());
         customerDTO.setName(customer1.getName());
         //customerDTO.setCustomerRetrievalRate(customer1.getCustomConfig().getCustomerRetrievalRate());
         return customerDTO;
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());

       // customerDTO.setCustomerRetrievalRate(customer.getCustomConfig().getCustomerRetrievalRate());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setName(customerDTO.getName());
        return customer;
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
