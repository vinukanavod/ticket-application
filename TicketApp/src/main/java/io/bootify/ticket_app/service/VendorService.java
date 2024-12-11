package io.bootify.ticket_app.service;

import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.domain.Vendor;
import io.bootify.ticket_app.model.VendorDTO;
import io.bootify.ticket_app.repos.TicketRepository;
import io.bootify.ticket_app.repos.VendorRepository;
import io.bootify.ticket_app.util.NotFoundException;
import io.bootify.ticket_app.util.ReferencedWarning;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final TicketRepository ticketRepository;

    public VendorService(final VendorRepository vendorRepository, final TicketRepository ticketRepository) {
        this.vendorRepository = vendorRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("id"));
        return vendors.stream()
                .map(this::mapToVendorDTO)
                .toList();
    }

    public VendorDTO get(final Long id) {
        return vendorRepository.findById(id)
                .map(this::mapToVendorDTO)
                .orElseThrow(NotFoundException::new);
    }

    public VendorDTO create(final VendorDTO vendorDTO) {
        if (vendorDTO.getName() == null) {
            throw new IllegalArgumentException("Vendor name is required");
        }
        final Vendor vendor = vendorRepository.save(new Vendor(vendorDTO.getName()));
        return mapToVendorDTO(vendor);
    }

    public VendorDTO update(final Long id, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        vendor.setMaxTicketCapacity(vendorDTO.getMaxTicketCapacity() != null ? vendorDTO.getMaxTicketCapacity() : vendor.getMaxTicketCapacity());
        vendor.setTicketReleaseRate(vendorDTO.getTicketReleaseRate() != null ? vendorDTO.getTicketReleaseRate() : vendor.getTicketReleaseRate());
        vendorRepository.save(vendor);

        return mapToVendorDTO(vendor);
    }

    public void delete(final Long id) {
        vendorRepository.deleteById(id);
    }

    public VendorDTO issueTickets(final Long id, final Integer numberOfTickets) {
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        if (vendor.getTotalTickets() == null) {
            vendor.setTotalTickets(0);
        } else if (vendor.getTotalTickets() + numberOfTickets > vendor.getMaxTicketCapacity()) {
            throw new IllegalArgumentException("Vendor has reached max ticket capacity");
        }
        vendor.setTotalTickets(vendor.getTotalTickets() + numberOfTickets);
        vendorRepository.save(vendor);
        return mapToVendorDTO(vendor);
    }

    public VendorDTO mapToVendorDTO(final Vendor vendor) {
        return mapToVendorDTO(vendor, new VendorDTO());
    }

    public VendorDTO mapToVendorDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setId(vendor.getId());
        vendorDTO.setName(vendor.getName());
        vendorDTO.setTotalTickets(vendor.getTotalTickets());
        vendorDTO.setMaxTicketCapacity(vendor.getMaxTicketCapacity());
        vendorDTO.setTicketReleaseRate(vendor.getTicketReleaseRate());
        return vendorDTO;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket vendorIdTicket = ticketRepository.findFirstByVendorId(vendor);
        if (vendorIdTicket != null) {
            referencedWarning.setKey("vendor.ticket.vendorId.referenced");
            referencedWarning.addParam(vendorIdTicket.getId());
            return referencedWarning;
        }
        return null;
    }

}
