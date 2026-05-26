package com.smartqueue.service;

import com.smartqueue.dto.TicketRequest;
import com.smartqueue.dto.TicketResponse;
import com.smartqueue.events.TicketCreatedEvent;
import com.smartqueue.events.TicketResolvedEvent;
import com.smartqueue.exception.TicketNotFoundException;
import com.smartqueue.exception.UserNotFoundException;
import com.smartqueue.model.Comment;
import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Status;
import com.smartqueue.repository.CommentRepository;
import com.smartqueue.repository.TicketRepository;
import com.smartqueue.repository.UserRepository;
import com.smartqueue.routing.RoutingEngine;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private static final Logger log = Logger.getLogger(TicketService.class.getName());

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RoutingEngine routingEngine;
    private final ApplicationEventPublisher eventPublisher;

    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         CommentRepository commentRepository,
                         RoutingEngine routingEngine,
                         ApplicationEventPublisher eventPublisher) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.routingEngine = routingEngine;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TicketResponse createTicket(TicketRequest request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setCreatedBy(creator);
        ticket = ticketRepository.save(ticket);

        User assignedAgent = routingEngine.routeTicket(ticket);
        if (assignedAgent != null) {
            ticket.setAssignedTo(assignedAgent);
            ticket.setStatus(Status.IN_PROGRESS);
            ticket = ticketRepository.save(ticket);
        }

        eventPublisher.publishEvent(new TicketCreatedEvent(this, ticket));
        log.info("Ticket created: " + ticket.getId() + " by " + userEmail);
        return mapToResponse(ticket);
    }

    @Transactional
    public TicketResponse updateStatus(Long ticketId, Status newStatus, String userEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        ticket.setStatus(newStatus);
        if (newStatus == Status.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
            eventPublisher.publishEvent(new TicketResolvedEvent(this, ticket));
        }
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public void addComment(Long ticketId, String content, String userEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTicket(ticket);
        comment.setAuthor(author);
        commentRepository.save(comment);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<TicketResponse> getMyTickets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ticketRepository.findByCreatedBy(user).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<TicketResponse> getAssignedTickets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ticketRepository.findByAssignedTo(user).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        return mapToResponse(ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id)));
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse r = new TicketResponse();
        r.setId(ticket.getId());
        r.setTitle(ticket.getTitle());
        r.setDescription(ticket.getDescription());
        r.setPriority(ticket.getPriority());
        r.setStatus(ticket.getStatus());
        r.setCategory(ticket.getCategory());
        r.setEscalated(ticket.isEscalated());
        r.setSlaBreach(ticket.isSlaBreach());
        r.setCreatedAt(ticket.getCreatedAt());
        r.setSlaDeadline(ticket.getSlaDeadline());
        if (ticket.getCreatedBy() != null)
            r.setCreatedByName(ticket.getCreatedBy().getName());
        if (ticket.getAssignedTo() != null)
            r.setAssignedToName(ticket.getAssignedTo().getName());
        return r;
    }
}
