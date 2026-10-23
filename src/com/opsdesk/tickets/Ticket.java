package com.opsdesk.tickets;

public abstract class Ticket implements Assignable {

    private int id;
    private String title;
    private String status;
    private String priority;
    private String assignee;

    public Ticket(int id, String title, String status, String priority) {
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

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    @Override
    public void assignTo(String user) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Assignee name cannot be empty.");
        }
        this.assignee = user;
    }

    public abstract String describe();

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
