package com.opsdesk.tickets;

public abstract class Ticket implements Assignable {

    private final int id;
    private final String title;
    private TicketStatus status;
    private final Priority priority;
    private String assignee;

    public Ticket(int id, String title, TicketStatus status, Priority priority) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void changeStatus(TicketStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
        this.status = newStatus;
    }

    @Override
    public void assignTo(String user) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Assignee name cannot be empty.");
        }
        this.assignee = user;
    }

    public abstract String describe();

    // Shared wording for describe(), so each subclass only supplies what is different
    protected String describeAs(String kind, String detail) {
        String owner = assignee == null ? "unassigned" : "assigned to " + assignee;
        return kind + " #" + id + " [" + priority + ", " + status + "]: " + title
                + " (" + detail + ") - " + owner;
    }

    // Two tickets are the same ticket if they have the same id
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Ticket that)) {
            return false;
        }
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
