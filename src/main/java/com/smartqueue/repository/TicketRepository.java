package com.smartqueue.repository;

import com.smartqueue.model.Ticket;
import com.smartqueue.model.User;
import com.smartqueue.model.enums.Category;
import com.smartqueue.model.enums.Priority;
import com.smartqueue.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByAssignedTo(User user);
    List<Ticket> findByStatus(Status status);
    List<Ticket> findByStatusIn(List<Status> statuses);
    List<Ticket> findByPriority(Priority priority);
    List<Ticket> findByCategory(Category category);
    List<Ticket> findByEscalatedTrue();
    List<Ticket> findBySlaBreachTrue();
    long countByAssignedToAndStatus(User user, Status status);

    @Query("SELECT t FROM Ticket t WHERE t.assignedTo = ?1 AND t.status IN ('OPEN', 'IN_PROGRESS')")
    List<Ticket> findActiveTicketsByAgent(User agent);
}