package com.smartqueue.routing;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import java.util.List;

public interface RoutingStrategy {
    User selectAgent(Ticket ticket, List<User> availableAgents);
    String getStrategyName();
}