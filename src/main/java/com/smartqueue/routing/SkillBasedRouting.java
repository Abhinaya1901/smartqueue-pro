package com.smartqueue.routing;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.TicketRepository;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("SKILL_BASED")
public class SkillBasedRouting implements RoutingStrategy {

    private final TicketRepository ticketRepository;

    public SkillBasedRouting(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public User selectAgent(Ticket ticket, List<User> availableAgents) {
        List<User> specialized = availableAgents.stream()
                .filter(a -> a.getSpecialization() == ticket.getCategory())
                .collect(Collectors.toList());
        List<User> pool = specialized.isEmpty() ? availableAgents : specialized;
        return pool.stream()
                .min(Comparator.comparingLong(agent ->
                        ticketRepository.countByAssignedToAndStatus(agent, Status.IN_PROGRESS)))
                .orElseThrow(() -> new RuntimeException("No agents available"));
    }

    @Override
    public String getStrategyName() { return "SKILL_BASED"; }
}
