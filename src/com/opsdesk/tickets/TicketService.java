package com.opsdesk.tickets;

import java.util.List;

public class TicketService {

    private final TicketRepository repository = new TicketRepository();
    private int nextId = 1;

    public Ticket createSupportTicket(String title, Priority priority, String category) {
        return addNew(new SupportTicket(nextId++, title, TicketStatus.OPEN, priority, category));
    }

    public Ticket createAssetRequest(String title, Priority priority, String assetName) {
        return addNew(new AssetRequest(nextId++, title, TicketStatus.OPEN, priority, assetName));
    }

    private Ticket addNew(Ticket ticket) {
        repository.add(ticket);
        return ticket;
    }

    // Adds tickets loaded from a file; new tickets get ids after the highest loaded id
    public void addExistingTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            repository.add(ticket);
            nextId = Math.max(nextId, ticket.getId() + 1);
        }
    }

    public Ticket getById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public Ticket assign(int id, String user) {
        Ticket ticket = getById(id);
        ticket.assignTo(user);
        return ticket;
    }

    public Ticket changeStatus(int id, TicketStatus newStatus) {
        Ticket ticket = getById(id);
        ticket.changeStatus(newStatus);
        return ticket;
    }

    public List<Ticket> getAll() {
        return repository.getAll();
    }

    public List<Ticket> listByPriority() {
        return repository.sortedByPriorityThenTitle();
    }

    public List<Ticket> filterByStatus(TicketStatus status) {
        return repository.filterByStatus(status);
    }

    public List<TicketSummary> summaries() {
        return repository.summaries();
    }
}
