package com.opsdesk.tickets;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Saves tickets as one line each: type,id,title,status,priority,assignee,detail
// Every field is URL-encoded, so commas, quotes and line breaks in titles
// become %2C, %22, %0A and can never break the format.
public class TicketFileStore {

    public record LoadResult(List<Ticket> tickets, List<String> warnings) {
    }

    private static final String SEPARATOR = ",";
    private static final int FIELD_COUNT = 7;
    private static final String SUPPORT = "SUPPORT";
    private static final String ASSET = "ASSET";

    private final Path file;

    public TicketFileStore(Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    public void save(List<Ticket> tickets) throws IOException {
        List<String> lines = tickets.stream()
                .map(this::toLine)
                .collect(Collectors.toList());
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    public LoadResult load() throws IOException {
        if (Files.notExists(file)) {
            return new LoadResult(List.of(), List.of());
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Ticket> tickets = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            int lineNumber = i + 1;
            try {
                Ticket ticket = fromLine(line);
                if (seenIds.add(ticket.getId())) {
                    tickets.add(ticket);
                } else {
                    warnings.add("Line " + lineNumber + " skipped: duplicate ticket id " + ticket.getId() + ".");
                }
            } catch (IllegalArgumentException e) {
                warnings.add("Line " + lineNumber + " skipped: " + e.getMessage());
            }
        }
        return new LoadResult(tickets, warnings);
    }

    private String toLine(Ticket ticket) {
        String type;
        String detail;
        if (ticket instanceof SupportTicket support) {
            type = SUPPORT;
            detail = support.getCategory();
        } else if (ticket instanceof AssetRequest asset) {
            type = ASSET;
            detail = asset.getAssetName();
        } else {
            throw new IllegalArgumentException("Unknown ticket type: " + ticket.getClass().getSimpleName());
        }
        String assignee = ticket.getAssignee() == null ? "" : ticket.getAssignee();
        return Arrays.stream(new String[] {
                        type,
                        String.valueOf(ticket.getId()),
                        ticket.getTitle(),
                        ticket.getStatus().name(),
                        ticket.getPriority().name(),
                        assignee,
                        detail})
                .map(field -> URLEncoder.encode(field, StandardCharsets.UTF_8))
                .collect(Collectors.joining(SEPARATOR));
    }

    private Ticket fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        if (fields.length != FIELD_COUNT) {
            throw new IllegalArgumentException(
                    "expected " + FIELD_COUNT + " fields but found " + fields.length + ".");
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = URLDecoder.decode(fields[i], StandardCharsets.UTF_8);
        }
        String type = fields[0];
        int id = parseId(fields[1]);
        String title = fields[2];
        TicketStatus status = parseEnum(TicketStatus.class, fields[3], "status");
        Priority priority = parseEnum(Priority.class, fields[4], "priority");
        String assignee = fields[5];
        String detail = fields[6];

        Ticket ticket = switch (type) {
            case SUPPORT -> new SupportTicket(id, title, status, priority, detail);
            case ASSET -> new AssetRequest(id, title, status, priority, detail);
            default -> throw new IllegalArgumentException("unknown ticket type \"" + type + "\".");
        };
        if (!assignee.isEmpty()) {
            ticket.assignTo(assignee);
        }
        return ticket;
    }

    private int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ticket id \"" + value + "\" is not a whole number.");
        }
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumType, String value, String fieldName) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " \"" + value + "\" is not recognised.");
        }
    }
}
