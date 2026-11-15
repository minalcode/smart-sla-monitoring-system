package com.artificial.smart_sla_monitoring_system.Repository;

import com.artificial.smart_sla_monitoring_system.Model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByStatus(String status);

    List<TicketEntity> findByEscalatedTrue();
}