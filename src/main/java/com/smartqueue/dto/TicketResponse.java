package com.smartqueue.dto;

import com.smartqueue.model.enums.Category;
import com.smartqueue.model.enums.Priority;
import com.smartqueue.model.enums.Status;
import java.time.LocalDateTime;

public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private Category category;
    private String createdByName;
    private String assignedToName;
    private boolean escalated;
    private boolean slaBreach;
    private LocalDateTime createdAt;
    private LocalDateTime slaDeadline;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    public boolean isEscalated() { return escalated; }
    public void setEscalated(boolean escalated) { this.escalated = escalated; }
    public boolean isSlaBreach() { return slaBreach; }
    public void setSlaBreach(boolean slaBreach) { this.slaBreach = slaBreach; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }
}
