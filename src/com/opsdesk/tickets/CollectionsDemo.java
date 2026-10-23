package com.opsdesk.tickets;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsDemo {

    public static void main(String[] args) {
        TicketRepository repo = new TicketRepository();
        repo.add(new SupportTicket(1, "VPN drops every hour", "OPEN", "HIGH", "Network"));
        repo.add(new AssetRequest(2, "Second monitor", "OPEN", "LOW", "Dell 24 monitor"));
        repo.add(new SupportTicket(3, "Printer jam on floor 2", "IN_PROGRESS", "MEDIUM", "Hardware"));
        repo.add(new AssetRequest(4, "Laptop for new starter", "CLOSED", "HIGH", "Dell Latitude 5440"));
        repo.add(new SupportTicket(5, "Email not syncing", "IN_PROGRESS", "LOW", "Software"));

        List<Ticket> all = repo.getAll();
        all.get(0).assignTo("Amaka");
        all.get(1).assignTo("Tunde");
        all.get(2).assignTo("Amaka");
        all.get(3).assignTo("Bola");
        all.get(4).assignTo("Tunde");

        System.out.println("--- List: all tickets (" + all.size() + ") ---");
        repo.printAll();

        System.out.println("--- Set: unique assignees ---");
        Set<String> assignees = repo.getUniqueAssignees();
        System.out.println(assignees + " (" + assignees.size() + " people for 5 tickets)");

        System.out.println("--- Map: look up by id ---");
        Map<String, Ticket> byId = repo.indexById();
        System.out.println("Ticket \"3\": " + byId.get("3").getTitle());
        System.out.println("Ticket \"99\": " + byId.get("99"));

        System.out.println("--- Map: grouped by status ---");
        Map<String, List<Ticket>> byStatus = repo.groupByStatus();
        for (Map.Entry<String, List<Ticket>> entry : byStatus.entrySet()) {
            System.out.print(entry.getKey() + ":");
            for (Ticket ticket : entry.getValue()) {
                System.out.print(" #" + ticket.getId());
            }
            System.out.println();
        }

        System.out.println("--- Sorted by priority, then title ---");
        for (Ticket ticket : repo.sortedByPriorityThenTitle()) {
            System.out.println(ticket.getPriority() + "  " + ticket.getTitle());
        }

        System.out.println("--- equals and hashCode ---");
        Ticket first = new SupportTicket(10, "Reset my password", "OPEN", "LOW", "Accounts");
        Ticket second = new SupportTicket(10, "Password reset please", "OPEN", "HIGH", "Accounts");
        System.out.println("first == second: " + (first == second));
        System.out.println("first.equals(second): " + first.equals(second));
        Set<Ticket> unique = new HashSet<>();
        unique.add(first);
        unique.add(second);
        System.out.println("HashSet size after adding both (expected 1): " + unique.size());
    }
}
