package com.opsdesk.tickets;

import java.util.List;
import java.util.Optional;

public class ModernJavaDemo {

    public static void main(String[] args) {
        TicketRepository repo = new TicketRepository();
        repo.add(new SupportTicket(1, "VPN drops every hour", TicketStatus.OPEN, Priority.HIGH, "Network"));
        repo.add(new AssetRequest(2, "Second monitor", TicketStatus.OPEN, Priority.LOW, "Dell 24 monitor"));
        repo.add(new SupportTicket(3, "Printer jam on floor 2", TicketStatus.IN_PROGRESS, Priority.MEDIUM, "Hardware"));
        repo.add(new AssetRequest(4, "Laptop for new starter", TicketStatus.RESOLVED, Priority.HIGH, "Dell Latitude 5440"));

        System.out.println("--- Enum: sorted by priority (HIGH first), then title ---");
        repo.sortedByPriorityThenTitle()
                .forEach(ticket -> System.out.println(ticket.getPriority() + "  " + ticket.getTitle()));

        System.out.println("--- Streams: filter OPEN tickets ---");
        repo.filterByStatus(TicketStatus.OPEN)
                .forEach(ticket -> System.out.println("#" + ticket.getId() + " " + ticket.getTitle()));

        System.out.println("--- Record: ticket summaries ---");
        List<TicketSummary> summaries = repo.summaries();
        summaries.forEach(System.out::println);
        System.out.println("First summary's title: " + summaries.get(0).title());

        System.out.println("--- Optional: findById ---");
        Optional<Ticket> found = repo.findById(3);
        Optional<Ticket> missing = repo.findById(99);
        System.out.println("findById(3): " + found.map(Ticket::getTitle).orElse("no such ticket"));
        System.out.println("findById(99): " + missing.map(Ticket::getTitle).orElse("no such ticket"));

        System.out.println("--- Exception: unknown id, caught in one place ---");
        TicketService service = new TicketService();
        service.createSupportTicket("Reset my password", Priority.LOW, "Accounts");
        try {
            System.out.println("getById(1): " + service.getById(1).getTitle());
            System.out.println("getById(42): " + service.getById(42).getTitle());
            System.out.println("This line is never reached.");
        } catch (TicketNotFoundException e) {
            System.out.println("Sorry: " + e.getMessage());
        }
    }
}
