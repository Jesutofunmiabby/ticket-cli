package com.opsdesk.tickets;

import java.util.ArrayList;
import java.util.List;

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
}
