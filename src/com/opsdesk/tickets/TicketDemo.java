package com.opsdesk.tickets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

// Checks every TicketService feature plus saving and loading, without JUnit.
// Each check prints PASS or FAIL with the expected and actual values.
public class TicketDemo {

    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        TicketService service = new TicketService();
        String trickyTitle = "Printer jam, floor 2 \"urgent\" 100% & 50/50; done?";

        System.out.println("--- Create ---");
        Ticket printer = service.createSupportTicket(trickyTitle, Priority.MEDIUM, "Hardware");
        Ticket laptop = service.createAssetRequest("Laptop for new starter", Priority.HIGH, "Dell Latitude 5440");
        Ticket email = service.createSupportTicket("Email not syncing", Priority.LOW, "Software");
        check("first ticket gets id 1", 1, printer.getId());
        check("second ticket gets id 2", 2, laptop.getId());
        check("asset request has the right type", "AssetRequest", laptop.getClass().getSimpleName());
        check("new tickets start OPEN", TicketStatus.OPEN, email.getStatus());

        System.out.println("--- Find by id ---");
        check("getById(2) finds the laptop", "Laptop for new starter", service.getById(2).getTitle());
        check("getById(99) throws TicketNotFoundException",
                "TicketNotFoundException", exceptionThrownBy(() -> service.getById(99)));

        System.out.println("--- Assign ---");
        service.assign(1, "Amaka");
        check("assign sets the assignee", "Amaka", service.getById(1).getAssignee());
        check("assign to unknown id throws TicketNotFoundException",
                "TicketNotFoundException", exceptionThrownBy(() -> service.assign(99, "Amaka")));
        check("assign to a blank name throws IllegalArgumentException",
                "IllegalArgumentException", exceptionThrownBy(() -> service.assign(1, "  ")));

        System.out.println("--- Change status ---");
        service.changeStatus(1, TicketStatus.IN_PROGRESS);
        check("changeStatus updates the status", TicketStatus.IN_PROGRESS, service.getById(1).getStatus());
        check("changeStatus on unknown id throws TicketNotFoundException",
                "TicketNotFoundException", exceptionThrownBy(() -> service.changeStatus(99, TicketStatus.CLOSED)));

        System.out.println("--- List and filter ---");
        check("listByPriority puts HIGH first, LOW last",
                List.of("Laptop for new starter", trickyTitle, "Email not syncing"),
                service.listByPriority().stream().map(Ticket::getTitle).toList());
        check("filterByStatus(IN_PROGRESS) finds ticket 1",
                List.of(1), service.filterByStatus(TicketStatus.IN_PROGRESS).stream().map(Ticket::getId).toList());
        check("filterByStatus(OPEN) finds tickets 2 and 3",
                List.of(2, 3), service.filterByStatus(TicketStatus.OPEN).stream().map(Ticket::getId).toList());
        check("filterByStatus(CLOSED) finds nothing",
                List.of(), service.filterByStatus(TicketStatus.CLOSED));
        check("summaries returns TicketSummary records",
                new TicketSummary("1", trickyTitle, TicketStatus.IN_PROGRESS), service.summaries().get(0));
        check("getAll returns every ticket", 3, service.getAll().size());

        checkSaveAndLoad(service);

        System.out.println();
        System.out.println(passed + "/" + total + " passed");
        if (passed != total) {
            System.exit(1);
        }
    }

    // Any IOException is reported as a FAIL instead of stopping the run
    private static void checkSaveAndLoad(TicketService service) {
        System.out.println("--- Save and load ---");
        Path file = null;
        try {
            file = Files.createTempFile("tickets-demo", ".txt");
            TicketFileStore store = new TicketFileStore(file);
            store.save(service.getAll());
            TicketFileStore.LoadResult loaded = store.load();
            check("load returns as many tickets as were saved", 3, loaded.tickets().size());
            check("loaded tickets keep every field, including the tricky title",
                    describeAll(service.getAll()), describeAll(loaded.tickets()));
            check("a clean file loads with no warnings", 0, loaded.warnings().size());

            TicketService reloaded = new TicketService();
            reloaded.addExistingTickets(loaded.tickets());
            Ticket next = reloaded.createSupportTicket("After reload", Priority.LOW, "Other");
            check("ids continue after the highest loaded id", 4, next.getId());

            Files.writeString(file, "SUPPORT,1,ok,OPEN,LOW,,Other\nnot a ticket\nASSET,abc,t,OPEN,LOW,,d\n");
            TicketFileStore.LoadResult broken = store.load();
            check("broken lines are skipped, good lines kept", 1, broken.tickets().size());
            check("each broken line gives one warning", 2, broken.warnings().size());

            Files.delete(file);
            TicketFileStore.LoadResult missing = store.load();
            check("a missing file loads as empty", 0, missing.tickets().size());
        } catch (IOException e) {
            check("save and load finish without an IOException", "no IOException", e.toString());
        } finally {
            deleteQuietly(file);
        }
    }

    private static void deleteQuietly(Path file) {
        if (file == null) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.out.println("Note: could not delete temporary file " + file);
        }
    }

    private static void check(String name, Object expected, Object actual) {
        total++;
        boolean ok = Objects.equals(expected, actual);
        if (ok) {
            passed++;
        }
        System.out.println((ok ? "PASS" : "FAIL") + "  " + name);
        System.out.println("      expected: " + expected);
        System.out.println("      actual:   " + actual);
    }

    // Runs the action and returns the name of the exception it threw, or "no exception"
    private static String exceptionThrownBy(Runnable action) {
        try {
            action.run();
            return "no exception";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

    private static List<String> describeAll(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::describe).toList();
    }
}
