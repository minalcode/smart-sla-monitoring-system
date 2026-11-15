package com.artificial.smart_sla_monitoring_system.Service;

import com.artificial.smart_sla_monitoring_system.Dto.TicketRequestDto;
import com.artificial.smart_sla_monitoring_system.Dto.TicketResponseDto;
import com.artificial.smart_sla_monitoring_system.Model.TicketEntity;
import com.artificial.smart_sla_monitoring_system.Repository.TicketRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Create Ticket
    public TicketResponseDto createTicket(TicketRequestDto request) {

        if (request == null) {
            throw new RuntimeException("Request body is required");
        }

        if (request.getTicketTitle() == null ||
                request.getTicketTitle().isBlank()) {
            throw new RuntimeException("Ticket title is required");
        }

        if (request.getDescription() == null ||
                request.getDescription().isBlank()) {
            throw new RuntimeException("Description is required");
        }

        if (request.getCustomerEmail() == null ||
                !request.getCustomerEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Valid customer email is required");
        }

        if (request.getPriority() == null) {
            throw new RuntimeException("Priority is required");
        }

        String priority = request.getPriority().toUpperCase();

        int slaHours;

        switch (priority) {
            case "LOW":
                slaHours = 48;
                break;

            case "MEDIUM":
                slaHours = 24;
                break;

            case "HIGH":
                slaHours = 8;
                break;

            case "CRITICAL":
                slaHours = 2;
                break;

            default:
                throw new RuntimeException(
                        "Priority must be LOW, MEDIUM, HIGH or CRITICAL");
        }

        TicketEntity ticket = new TicketEntity();

        ticket.setTicketTitle(request.getTicketTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCustomerEmail(request.getCustomerEmail());
        ticket.setPriority(priority);
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSlaDeadline(LocalDateTime.now().plusHours(slaHours));
        ticket.setEscalated(false);

        TicketEntity saved = ticketRepository.save(ticket);

        return convertToResponse(saved);
    }

    // Get All Tickets
    public List<TicketResponseDto> getAllTickets() {

        return ticketRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get Ticket By ID
    public TicketResponseDto getTicketById(Long id) {

        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        return convertToResponse(ticket);
    }

    // Update Ticket Status
    public TicketResponseDto updateTicketStatus(Long id, String status) {

        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        if (status == null ||
                (!status.equalsIgnoreCase("OPEN") &&
                        !status.equalsIgnoreCase("IN_PROGRESS") &&
                        !status.equalsIgnoreCase("RESOLVED"))) {
            throw new RuntimeException(
                    "Status must be OPEN, IN_PROGRESS or RESOLVED");
        }

        ticket.setStatus(status.toUpperCase());

        TicketEntity updated = ticketRepository.save(ticket);

        return convertToResponse(updated);
    }

    // Check SLA and Escalate Tickets
    @Transactional
    public List<TicketResponseDto> checkSlaAndEscalate() {

        List<TicketEntity> tickets = ticketRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        for (TicketEntity ticket : tickets) {

            if (!"RESOLVED".equals(ticket.getStatus()) &&
                    !ticket.isEscalated() &&
                    ticket.getSlaDeadline() != null &&
                    now.isAfter(ticket.getSlaDeadline())) {

                ticket.setEscalated(true);
                ticket.setStatus("ESCALATED");
            }
        }

        ticketRepository.saveAll(tickets);

        return tickets.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get Escalated Tickets
    public List<TicketResponseDto> getEscalatedTickets() {

        return ticketRepository.findByEscalatedTrue()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Convert Entity to DTO
    private TicketResponseDto convertToResponse(TicketEntity ticket) {

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTicketTitle(),
                ticket.getCustomerEmail(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getSlaDeadline(),
                ticket.isEscalated()
        );
    }
}