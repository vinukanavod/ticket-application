package io.bootify.ticket_app.rest;

import io.bootify.ticket_app.model.CustomerConfigDTO;
import io.bootify.ticket_app.model.CustomerDTO;
import io.bootify.ticket_app.service.CustomerService;
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

import javax.swing.text.DefaultEditorKit;


@RestController
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/hellow")
    public ResponseEntity<String> sayHellow() {
        return ResponseEntity.ok("say Hellow baby");
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @PostMapping("/save")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid final CustomerDTO customerDTO) {
        final CustomerDTO customerDTO1 = customerService.create(customerDTO);
        return new ResponseEntity<>(customerDTO1, HttpStatus.CREATED);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(name = "id") final Long id,
                                                                     @RequestBody @Valid final CustomerConfigDTO customerConfigDTO) {

        return ResponseEntity.ok(customerService.update(id, customerConfigDTO));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = customerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
