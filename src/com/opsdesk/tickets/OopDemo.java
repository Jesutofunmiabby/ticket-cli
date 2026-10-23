package com.opsdesk.tickets;

import java.util.ArrayList;
import java.util.List;

public class OopDemo {

    public static void main(String[] args) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new SupportTicket(1, "Cannot log in to VPN", TicketStatus.OPEN, Priority.HIGH, "Network"));
        tickets.add(new AssetRequest(2, "New laptop for new starter", TicketStatus.OPEN, Priority.MEDIUM, "Dell Latitude 5440"));

        System.out.println("--- Describe every ticket ---");
        for (Ticket ticket : tickets) {
            System.out.println(ticket.describe());
        }

        System.out.println("--- Assign tickets ---");
        tickets.get(0).assignTo("Amaka");
        tickets.get(1).assignTo("Tunde");
        for (Ticket ticket : tickets) {
            System.out.println("Ticket #" + ticket.getId() + " assigned to " + ticket.getAssignee());
        }

        System.out.println("--- Bank account ---");
        BankAccount account = new BankAccount(100.0);
        account.deposit(50.0);
        System.out.println("Balance after deposit: " + account.getBalance());
        try {
            account.withdraw(500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Sorry, that withdrawal was refused. " + e.getMessage());
        }
        System.out.println("Balance is still: " + account.getBalance());
    }
}
