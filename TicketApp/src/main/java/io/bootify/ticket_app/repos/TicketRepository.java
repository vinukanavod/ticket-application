package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.Customer;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findFirstByVendorId(Vendor vendor);

    Ticket findFirstByCustomerId(Customer customer);

    @Query("SELECT t FROM Ticket t WHERE t.vendorId = :vendor AND t.customerId = :customer")
    Ticket findByVendorAndCustomer(@Param("vendor") Vendor vendor, @Param("customer") Customer customer);

    @Query("SELECT sum(t.count) FROM Ticket t WHERE t.vendorId = :vendor")
    Integer findTicketsByVendor(@Param("vendor") Vendor vendor);

}
