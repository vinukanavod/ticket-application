package io.bootify.ticket_app.service;

import io.bootify.ticket_app.domain.SystemConfig;
import io.bootify.ticket_app.domain.Ticket;
import io.bootify.ticket_app.domain.Vendor;
import io.bootify.ticket_app.model.VendorDTO;
import io.bootify.ticket_app.repos.SystemConfigRepository;
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
    private final SystemConfigRepository systemConfigRepository;
    private final TicketRepository ticketRepository;

    public VendorService(final VendorRepository vendorRepository,
            final SystemConfigRepository systemConfigRepository,
            final TicketRepository ticketRepository) {
        this.vendorRepository = vendorRepository;
        this.systemConfigRepository = systemConfigRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("id"));
        return vendors.stream()
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .toList();
    }

    public VendorDTO get(final Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VendorDTO vendorDTO) {
        final Vendor vendor = new Vendor();
        mapToEntity(vendorDTO, vendor);
        return vendorRepository.save(vendor).getId();
    }

    public void update(final Long id, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorDTO, vendor);
        vendorRepository.save(vendor);
    }

    public void delete(final Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO mapToDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setId(vendor.getId());
        vendorDTO.setName(vendor.getName());
        vendorDTO.setSystemConfigId(vendor.getSystemConfigId() == null ? null : vendor.getSystemConfigId().getId());
        return vendorDTO;
    }

    private Vendor mapToEntity(final VendorDTO vendorDTO, final Vendor vendor) {
        vendor.setName(vendorDTO.getName());
        final SystemConfig systemConfigId = vendorDTO.getSystemConfigId() == null ? null : systemConfigRepository.findById(vendorDTO.getSystemConfigId())
                .orElseThrow(() -> new NotFoundException("systemConfigId not found"));
        vendor.setSystemConfigId(systemConfigId);
        return vendor;
    }

    public boolean systemConfigIdExists(final Long id) {
        return vendorRepository.existsBySystemConfigIdId(id);
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
