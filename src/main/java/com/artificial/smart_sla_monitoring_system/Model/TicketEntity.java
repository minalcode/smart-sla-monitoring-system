package com.artificial.smart_sla_monitoring_system.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticketTitle;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String customerEmail;

    private String priority;

    private String status = "OPEN";

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime slaDeadline;

    private boolean escalated = false;
}