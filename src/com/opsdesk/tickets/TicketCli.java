package com.opsdesk.tickets;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TicketCli {

    private final TicketService service = new TicketService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new TicketCli().run();
    }

    public void run() {
        System.out.println("=== OpsDesk Ticket CLI ===");
        try {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1) Create  2) Assign  3) Status  4) List  5) Filter  0) Quit");
                String choice = prompt("Choose an option: ");
                try {
                    running = handle(choice);
                } catch (TicketNotFoundException e) {
                    System.out.println("Sorry: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // Input ended (Ctrl+D, Ctrl+Z or the end of a piped file): stop instead of crashing
        }
        System.out.println("Goodbye.");
    }

    // Returns false when the user wants to quit
    private boolean handle(String choice) {
        switch (choice) {
            case "1" -> create();
            case "2", "3", "5" -> System.out.println("That option is not available yet.");
            case "4" -> list();
            case "0" -> {
                return false;
            }
            default -> System.out.println("\"" + choice + "\" is not an option. Please enter a number from 0 to 5.");
        }
        return true;
    }

    private void create() {
        String type = readType();
        String title = readNonBlank("Title: ");
        Priority priority = readPriority();
        Ticket ticket;
        if (type.equals("1")) {
            String category = readNonBlank("Category: ");
            ticket = service.createSupportTicket(title, priority, category);
        } else {
            String assetName = readNonBlank("Asset name: ");
            ticket = service.createAssetRequest(title, priority, assetName);
        }
        System.out.println("Created: " + ticket.describe());
    }

    private void list() {
        List<Ticket> tickets = service.listByPriority();
        if (tickets.isEmpty()) {
            System.out.println("No tickets yet.");
            return;
        }
        tickets.forEach(ticket -> System.out.println(ticket.describe()));
    }

    private String readType() {
        while (true) {
            String type = prompt("Type (1 = Support ticket, 2 = Asset request): ");
            if (type.equals("1") || type.equals("2")) {
                return type;
            }
            System.out.println("Please enter 1 or 2.");
        }
    }

    private String readNonBlank(String label) {
        while (true) {
            String value = prompt(label);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This cannot be empty.");
        }
    }

    private Priority readPriority() {
        while (true) {
            String value = prompt("Priority (HIGH, MEDIUM, LOW): ").toUpperCase();
            try {
                return Priority.valueOf(value);
            } catch (IllegalArgumentException e) {
                System.out.println("Please type HIGH, MEDIUM or LOW.");
            }
        }
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
