package com.smartqueue.routing;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.TicketRepository;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component("LOAD_BALANCED")
public class LoadBalancedRouting implements RoutingStrategy {

    private final TicketRepository ticketRepository;

    public LoadBalancedRouting(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public User selectAgent(Ticket ticket, List<User> availableAgents) {
        return availableAgents.stream()
                .min(Comparator.comparingLong(agent ->
                        ticketRepository.countByAssignedToAndStatus(agent, Status.IN_PROGRESS)))
                .orElseThrow(() -> new RuntimeException("No agents available"));
    }

    @Override
    public String getStrategyName() { return "LOAD_BALANCED"; }
}
