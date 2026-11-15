package com.artificial.smart_sla_monitoring_system.Dto;

import lombok.Data;

@Data
public class TicketRequestDto {

    private String ticketTitle;

    private String description;

    private String customerEmail;

    private String priority;
}