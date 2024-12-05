package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
}
