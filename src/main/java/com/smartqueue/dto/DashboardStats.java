package com.smartqueue.dto;

import java.util.Map;

public class DashboardStats {
    private long totalTickets;
    private long openTickets;
    private long inProgressTickets;
    private long resolvedTickets;
    private long escalatedTickets;
    private long slaBreachCount;
    private Map<String, Long> ticketsByCategory;
    private Map<String, Long> ticketsByPriority;

    public long getTotalTickets() { return totalTickets; }
    public void setTotalTickets(long v) { this.totalTickets = v; }
    public long getOpenTickets() { return openTickets; }
    public void setOpenTickets(long v) { this.openTickets = v; }
    public long getInProgressTickets() { return inProgressTickets; }
    public void setInProgressTickets(long v) { this.inProgressTickets = v; }
    public long getResolvedTickets() { return resolvedTickets; }
    public void setResolvedTickets(long v) { this.resolvedTickets = v; }
    public long getEscalatedTickets() { return escalatedTickets; }
    public void setEscalatedTickets(long v) { this.escalatedTickets = v; }
    public long getSlaBreachCount() { return slaBreachCount; }
    public void setSlaBreachCount(long v) { this.slaBreachCount = v; }
    public Map<String, Long> getTicketsByCategory() { return ticketsByCategory; }
    public void setTicketsByCategory(Map<String, Long> v) { this.ticketsByCategory = v; }
    public Map<String, Long> getTicketsByPriority() { return ticketsByPriority; }
    public void setTicketsByPriority(Map<String, Long> v) { this.ticketsByPriority = v; }
}
