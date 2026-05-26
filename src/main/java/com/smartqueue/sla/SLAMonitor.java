package com.smartqueue.sla;

import com.smartqueue.events.TicketEscalatedEvent;
import com.smartqueue.model.Ticket;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class SLAMonitor {

    private static final Logger log = Logger.getLogger(SLAMonitor.class.getName());
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public SLAMonitor(TicketRepository ticketRepository,
                      ApplicationEventPublisher eventPublisher) {
        this.ticketRepository = ticketRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkSLABreaches, 30, 30, TimeUnit.SECONDS);
        log.info("SLA Monitor started");
    }

    private void checkSLABreaches() {
        try {
            List<Ticket> active = ticketRepository.findByStatusIn(
                    List.of(Status.OPEN, Status.IN_PROGRESS));
            for (Ticket ticket : active) {
                if (isSLABreached(ticket)) {
                    ticket.setSlaBreach(true);
                    ticket.setEscalated(true);
                    ticket.setStatus(Status.ESCALATED);
                    ticketRepository.save(ticket);
                    eventPublisher.publishEvent(new TicketEscalatedEvent(this, ticket));
                    log.warning("SLA BREACH: Ticket [" + ticket.getId() +
                            "] - " + ticket.getTitle());
                }
            }
        } catch (Exception e) {
            log.severe("SLA check error: " + e.getMessage());
        }
    }

    private boolean isSLABreached(Ticket ticket) {
        if (ticket.getSlaDeadline() == null) return false;
        if (ticket.isSlaBreach()) return false;
        return LocalDateTime.now().isAfter(ticket.getSlaDeadline());
    }

    @PreDestroy
    public void stopMonitoring() {
        scheduler.shutdown();
    }
}
