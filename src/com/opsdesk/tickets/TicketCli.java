package com.opsdesk.tickets;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TicketCli {

    private final TicketService service;
    private final TicketFileStore fileStore;
    private final Scanner scanner = new Scanner(System.in);

    public TicketCli(TicketService service, TicketFileStore fileStore) {
        this.service = service;
        this.fileStore = fileStore;
    }

    public static void main(String[] args) {
        new TicketCli(new TicketService(), new TicketFileStore(Path.of("tickets.txt"))).run();
    }

    public void run() {
        System.out.println("=== OpsDesk Ticket CLI ===");
        loadTickets();
        try {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("1) Create  2) Assign  3) Status  4) List  5) Filter  0) Quit");
                String choice = prompt("Choose an option: ");
                try {
                    running = handleChoice(choice);
                } catch (TicketNotFoundException e) {
                    System.out.println("Sorry: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // Input ended (Ctrl+D, Ctrl+Z or the end of a piped file): stop instead of crashing
        }
        saveTickets();
        System.out.println("Goodbye.");
    }

    private void loadTickets() {
        try {
            TicketFileStore.LoadResult result = fileStore.load();
            result.warnings().forEach(warning -> System.out.println("Warning: " + warning));
            service.addExistingTickets(result.tickets());
            System.out.println("Loaded " + result.tickets().size() + " ticket(s) from " + fileStore.getFile() + ".");
        } catch (IOException e) {
            System.out.println("Could not read " + fileStore.getFile() + " (" + e.getMessage() + "). Starting with no tickets.");
        }
    }

    private void saveTickets() {
        try {
            List<Ticket> tickets = service.getAll();
            fileStore.save(tickets);
            System.out.println("Saved " + tickets.size() + " ticket(s) to " + fileStore.getFile() + ".");
        } catch (IOException e) {
            System.out.println("Could not save tickets to " + fileStore.getFile() + " (" + e.getMessage() + ").");
        }
    }

    // Returns false when the user wants to quit
    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1" -> create();
            case "2" -> assign();
            case "3" -> changeStatus();
            case "4" -> list();
            case "5" -> filter();
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
        Priority priority = readEnum("Priority", Priority.class);
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

    private void assign() {
        int id = readId();
        service.getById(id);    // fail fast on an unknown id, before asking for a name
        String user = readNonBlank("Assign to: ");
        System.out.println("Assigned: " + service.assign(id, user).describe());
    }

    private void changeStatus() {
        int id = readId();
        service.getById(id);    // fail fast on an unknown id, before asking for a status
        TicketStatus newStatus = readEnum("New status", TicketStatus.class);
        System.out.println("Updated: " + service.changeStatus(id, newStatus).describe());
    }

    private void list() {
        printTickets(service.listByPriority(), "No tickets yet.");
    }

    private void filter() {
        TicketStatus status = readEnum("Show status", TicketStatus.class);
        printTickets(service.filterByStatus(status), "No tickets with status " + status + ".");
    }

    private void printTickets(List<Ticket> tickets, String messageIfEmpty) {
        if (tickets.isEmpty()) {
            System.out.println(messageIfEmpty);
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

    private int readId() {
        while (true) {
            String value = prompt("Ticket id: ");
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a ticket id as a whole number, for example 3.");
            }
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

    // Works for any enum: keeps asking until the input matches one of its values
    private <E extends Enum<E>> E readEnum(String label, Class<E> enumType) {
        String choices = Arrays.stream(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        while (true) {
            String value = prompt(label + " (" + choices + "): ").toUpperCase();
            try {
                return Enum.valueOf(enumType, value);
            } catch (IllegalArgumentException e) {
                System.out.println("Please type one of: " + choices + ".");
            }
        }
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
