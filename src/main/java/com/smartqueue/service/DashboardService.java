package com.smartqueue.service;

import com.smartqueue.dto.DashboardStats;
import com.smartqueue.model.Ticket;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.TicketRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TicketRepository ticketRepository;

    public DashboardService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Cacheable("dashboardStats")
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        List<Ticket> all = ticketRepository.findAll();

        stats.setTotalTickets(all.size());
        stats.setOpenTickets(ticketRepository.findByStatus(Status.OPEN).size());
        stats.setInProgressTickets(ticketRepository.findByStatus(Status.IN_PROGRESS).size());
        stats.setResolvedTickets(ticketRepository.findByStatus(Status.RESOLVED).size());
        stats.setEscalatedTickets(ticketRepository.findByEscalatedTrue().size());
        stats.setSlaBreachCount(ticketRepository.findBySlaBreachTrue().size());

        Map<String, Long> byCategory = all.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(), Collectors.counting()));
        stats.setTicketsByCategory(byCategory);

        Map<String, Long> byPriority = all.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPriority().name(), Collectors.counting()));
        stats.setTicketsByPriority(byPriority);

        return stats;
    }
}
