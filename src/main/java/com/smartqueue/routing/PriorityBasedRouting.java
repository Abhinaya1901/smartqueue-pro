package com.smartqueue.routing;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Priority;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.TicketRepository;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component("PRIORITY_BASED")
public class PriorityBasedRouting implements RoutingStrategy {

    private final TicketRepository ticketRepository;

    public PriorityBasedRouting(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public User selectAgent(Ticket ticket, List<User> availableAgents) {
        Status filterStatus = ticket.getPriority() == Priority.CRITICAL ?
                Status.OPEN : Status.IN_PROGRESS;
        return availableAgents.stream()
                .min(Comparator.comparingLong(agent ->
                        ticketRepository.countByAssignedToAndStatus(agent, filterStatus)))
                .orElseThrow(() -> new RuntimeException("No agents available"));
    }

    @Override
    public String getStrategyName() { return "PRIORITY_BASED"; }
}
