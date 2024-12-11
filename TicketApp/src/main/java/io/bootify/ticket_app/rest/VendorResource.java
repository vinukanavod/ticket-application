package io.bootify.ticket_app.rest;

import io.bootify.ticket_app.model.VendorDTO;
import io.bootify.ticket_app.service.VendorService;
import io.bootify.ticket_app.util.ReferencedException;
import io.bootify.ticket_app.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/vendors", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendorResource {

    private final VendorService vendorService;

    public VendorResource(final VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(vendorService.get(id));
    }

    @PostMapping("/save")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<VendorDTO> createVendor(@RequestBody @Valid final VendorDTO vendorDTO) {
       VendorDTO vendorDTO1  = vendorService.create(vendorDTO);
        return new ResponseEntity<>(vendorDTO1, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<VendorDTO> updateVendor(@PathVariable(name = "id") final Long id, @RequestBody @Valid final VendorDTO vendorDTO) {
        return ResponseEntity.ok(vendorService.update(id, vendorDTO));
    }

    @GetMapping("/issue/{id}/{count}")
    public ResponseEntity<VendorDTO> issueTickets(@PathVariable(name = "id") final Long id, @PathVariable(name = "count") final Integer count) {
        return new ResponseEntity<>(vendorService.issueTickets(id, count), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVendor(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = vendorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        vendorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
