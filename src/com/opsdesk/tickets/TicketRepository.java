package com.opsdesk.tickets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TicketRepository {

    private final List<Ticket> tickets = new ArrayList<>();

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getAll() {
        return List.copyOf(tickets);
    }

    public void printAll() {
        for (Ticket ticket : tickets) {
            System.out.println(ticket.describe());
        }
    }

    public Set<String> getUniqueAssignees() {
        Set<String> assignees = new HashSet<>();
        for (Ticket ticket : tickets) {
            if (ticket.getAssignee() != null) {
                assignees.add(ticket.getAssignee());
            }
        }
        return assignees;
    }

    public Map<String, Ticket> indexById() {
        Map<String, Ticket> byId = new HashMap<>();
        for (Ticket ticket : tickets) {
            byId.put(String.valueOf(ticket.getId()), ticket);
        }
        return byId;
    }

    public Map<String, List<Ticket>> groupByStatus() {
        Map<String, List<Ticket>> byStatus = new HashMap<>();
        for (Ticket ticket : tickets) {
            String status = ticket.getStatus();
            if (!byStatus.containsKey(status)) {
                byStatus.put(status, new ArrayList<>());
            }
            byStatus.get(status).add(ticket);
        }
        return byStatus;
    }
}
