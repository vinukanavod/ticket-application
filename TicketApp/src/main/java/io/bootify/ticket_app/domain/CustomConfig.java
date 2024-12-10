package io.bootify.ticket_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public
class CustomConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer customerRetrievalRate;
    @Column
    private Integer maxTicketCapacity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id" , referencedColumnName = "id")

    private Customer customer;

}
