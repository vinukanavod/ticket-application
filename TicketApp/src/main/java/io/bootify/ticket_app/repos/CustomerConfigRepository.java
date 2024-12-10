package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.CustomConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerConfigRepository extends JpaRepository<CustomConfig, Long > {
}
