package com.smartqueue.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class TicketEventListener {

    private static final Logger log = Logger.getLogger(TicketEventListener.class.getName());

    @Async
    @EventListener
    public void handleTicketCreated(TicketCreatedEvent event) {
        log.info("New ticket created: [" + event.getTicket().getId() +
                "] - " + event.getTicket().getTitle());
    }

    @Async
    @EventListener
    public void handleTicketEscalated(TicketEscalatedEvent event) {
        log.warning("ESCALATION: Ticket [" + event.getTicket().getId() +
                "] has breached SLA!");
    }

    @Async
    @EventListener
    public void handleTicketResolved(TicketResolvedEvent event) {
        log.info("Ticket resolved: [" + event.getTicket().getId() +
                "] - " + event.getTicket().getTitle());
    }
}
