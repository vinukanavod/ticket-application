package io.bootify.ticket_app.rest;

import io.bootify.ticket_app.domain.Flag;
import io.bootify.ticket_app.model.TicketDTO;
import io.bootify.ticket_app.service.FlagService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/flag", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlagResource {
    private  final FlagService flagService;

    public FlagResource(FlagService flagService) {
        this.flagService = flagService;
    }

    @PutMapping("on")
    public ResponseEntity<Flag> SwitchONN( ) {
      Flag flag =  flagService.switchON();
        return ResponseEntity.ok(flag);
    }
    @PutMapping("off")
    public ResponseEntity<Flag> SwitchOFF( ) {
        Flag flag =  flagService.switchOFF();
        return ResponseEntity.ok(flag);
    }
    @GetMapping("get")
    public ResponseEntity<Flag> getStatus( ) {
        Flag flag =  flagService.getStatus();

        return ResponseEntity.ok(flag);
    }
}
