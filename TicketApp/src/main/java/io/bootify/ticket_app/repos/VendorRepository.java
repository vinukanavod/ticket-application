package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
