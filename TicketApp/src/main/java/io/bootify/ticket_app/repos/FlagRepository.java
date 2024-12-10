package io.bootify.ticket_app.repos;

import io.bootify.ticket_app.domain.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlagRepository extends JpaRepository<Flag , Long> {
}
