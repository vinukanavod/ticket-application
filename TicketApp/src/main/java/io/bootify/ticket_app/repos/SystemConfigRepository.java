package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
}
