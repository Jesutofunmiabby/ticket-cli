package com.opsdesk.tickets;

// Unchecked (extends RuntimeException) so it can travel up to the one place
// that handles it without every method in between declaring "throws"
public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(int id) {
        super("No ticket found with id " + id + ".");
    }
}
