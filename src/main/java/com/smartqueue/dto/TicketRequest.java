package com.smartqueue.dto;

import com.smartqueue.model.enums.Category;
import com.smartqueue.model.enums.Priority;

public class TicketRequest {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
