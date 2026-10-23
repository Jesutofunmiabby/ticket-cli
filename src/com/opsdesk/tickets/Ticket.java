package com.opsdesk.tickets;

public abstract class Ticket {

    private int id;
    private String title;
    private String status;
    private String priority;

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

    public abstract String describe();
}
