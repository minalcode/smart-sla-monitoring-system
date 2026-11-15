package com.artificial.smart_sla_monitoring_system.Controller;

import com.artificial.smart_sla_monitoring_system.Dto.TicketRequestDto;
import com.artificial.smart_sla_monitoring_system.Dto.TicketResponseDto;
import com.artificial.smart_sla_monitoring_system.Service.TicketService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Create Ticket
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(
            @RequestBody TicketRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(request));
    }

    // Get All Tickets
    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {

        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    // Get Ticket By ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    // Update Ticket Status
    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponseDto> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        return ResponseEntity.ok(
                ticketService.updateTicketStatus(
                        id, request.get("status")));
    }

    // Check SLA and Escalate
    @PostMapping("/check-sla")
    public ResponseEntity<List<TicketResponseDto>> checkSla() {

        return ResponseEntity.ok(
                ticketService.checkSlaAndEscalate());
    }

    // Get Escalated Tickets
    @GetMapping("/escalated")
    public ResponseEntity<List<TicketResponseDto>> getEscalatedTickets() {

        return ResponseEntity.ok(
                ticketService.getEscalatedTickets());
    }
}