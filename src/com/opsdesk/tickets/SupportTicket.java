package com.opsdesk.tickets;

public class SupportTicket extends Ticket {

    private String category;

    public SupportTicket(int id, String title, TicketStatus status, Priority priority, String category) {
        super(id, title, status, priority);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String describe() {
        return describeAs("Support ticket", "category: " + category);
    }
}
