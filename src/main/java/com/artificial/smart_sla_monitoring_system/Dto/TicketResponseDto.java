package com.artificial.smart_sla_monitoring_system.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketResponseDto {

    private Long id;

    private String ticketTitle;

    private String customerEmail;

    private String priority;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime slaDeadline;

    private boolean escalated;
}