package com.opsdesk.tickets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketRepository {

    private final List<Ticket> tickets = new ArrayList<>();

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getAll() {
        return List.copyOf(tickets);
    }

    public void printAll() {
        tickets.forEach(ticket -> System.out.println(ticket.describe()));
    }

    public Set<String> getUniqueAssignees() {
        return tickets.stream()
                .map(Ticket::getAssignee)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Map<String, Ticket> indexById() {
        return tickets.stream()
                .collect(Collectors.toMap(ticket -> String.valueOf(ticket.getId()), ticket -> ticket));
    }

    public Map<TicketStatus, List<Ticket>> groupByStatus() {
        return tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getStatus));
    }

    public List<Ticket> sortedByPriorityThenTitle() {
        return tickets.stream()
                .sorted(Comparator.comparing(Ticket::getPriority).thenComparing(Ticket::getTitle))
                .collect(Collectors.toList());
    }

    public List<Ticket> filterByStatus(TicketStatus status) {
        return tickets.stream()
                .filter(ticket -> ticket.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<TicketSummary> summaries() {
        return tickets.stream()
                .map(ticket -> new TicketSummary(
                        String.valueOf(ticket.getId()), ticket.getTitle(), ticket.getStatus()))
                .collect(Collectors.toList());
    }
}
