package io.bootify.ticket_app.model;

import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
    @AllArgsConstructor
public class CustomerConfigDTO {

    private Long id;

    @NonNull
    private Integer customerRetrievalRate;



}
