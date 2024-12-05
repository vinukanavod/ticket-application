package io.bootify.ticket_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @VendorSystemConfigIdUnique
    private Long systemConfigId;

}