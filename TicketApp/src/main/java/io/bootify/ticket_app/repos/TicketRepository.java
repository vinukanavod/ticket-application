package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.Customer;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findFirstByVendorId(Vendor vendor);

    Ticket findFirstByCustomerId(Customer customer);

    boolean existsByNumber(Integer number);


}
