package com.smartqueue.routing;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Priority;
import com.smartqueue.model.enums.Role;
import com.smartqueue.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class RoutingEngine {

    private static final Logger log = Logger.getLogger(RoutingEngine.class.getName());

    private final Map<String, RoutingStrategy> strategies;
    private final UserRepository userRepository;

    public RoutingEngine(Map<String, RoutingStrategy> strategies,
                         UserRepository userRepository) {
        this.strategies = strategies;
        this.userRepository = userRepository;
    }

    public User routeTicket(Ticket ticket) {
        List<User> availableAgents = userRepository.findByRoleAndActive(Role.AGENT, true);
        if (availableAgents.isEmpty()) {
            log.warning("No active agents available");
            return null;
        }
        String strategyName = determineStrategy(ticket);
        RoutingStrategy strategy = strategies.getOrDefault(strategyName,
                strategies.get("LOAD_BALANCED"));
        User selectedAgent = strategy.selectAgent(ticket, availableAgents);
        log.info("Ticket routed to agent [" + selectedAgent.getName() +
                "] using strategy [" + strategyName + "]");
        return selectedAgent;
    }

    private String determineStrategy(Ticket ticket) {
        if (ticket.getPriority() == Priority.CRITICAL) return "PRIORITY_BASED";
        if (ticket.getCategory() != null) return "SKILL_BASED";
        return "LOAD_BALANCED";
    }
}
