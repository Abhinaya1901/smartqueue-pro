package com.smartqueue.controller;

import com.smartqueue.dto.TicketRequest;
import com.smartqueue.dto.TicketResponse;
import com.smartqueue.model.enums.Status;
import com.smartqueue.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @RequestBody TicketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ticketService.createTicket(request, userDetails.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/my")
    public ResponseEntity<List<TicketResponse>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ticketService.getMyTickets(userDetails.getUsername()));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('AGENT', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAssignedTickets(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ticketService.getAssignedTickets(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('AGENT', 'MANAGER', 'ADMIN')")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ticketService.updateStatus(id, status, userDetails.getUsername()));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable Long id,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetails userDetails) {
        ticketService.addComment(id, content, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
