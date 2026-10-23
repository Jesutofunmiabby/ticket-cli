# ticket-cli

A command-line help-desk ticket tool written in plain Java 25, built as a
terminal companion to the OpsDesk project.

## What it does

From an interactive menu you can:

- **Create** a support ticket (with a category) or an asset request (with
  an asset name), giving it a title and a priority (HIGH, MEDIUM or LOW)
- **Assign** a ticket to a person
- **Change the status** of a ticket (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- **List** all tickets, highest priority first
- **Filter** tickets by status

Tickets are saved to `tickets.txt` when you quit and loaded again the next
time you start, so nothing is lost between runs. Typing letters, a wrong
number or an unknown ticket id shows a friendly message instead of crashing.

## Requirements

- Java 25 (JDK), with `java` and `javac` on your PATH

## Compile

From the project root (works in PowerShell and Git Bash):

```
javac -d out src/*.java src/com/opsdesk/tickets/*.java
```

Compiled `.class` files go into `out/`, which is not committed.

## Run the ticket CLI

```
java -cp out com.opsdesk.tickets.TicketCli
```

Choose an option by number: 1) Create, 2) Assign, 3) Status, 4) List,
5) Filter, 0) Quit. Choose 0 to save and exit.

`tickets.txt` is created in the folder you run the command from. It is
listed in `.gitignore`, so your tickets are never committed. Each ticket is
one line of comma-separated fields; every field is URL-encoded, so commas,
quotes or other special characters in a title cannot break the file. If a
line is damaged, it is skipped with a warning and the rest still load.

## Run the checks (TicketDemo)

```
java -cp out com.opsdesk.tickets.TicketDemo
```

`TicketDemo` tests every `TicketService` feature, plus saving and loading,
without any test library. Each check prints PASS or FAIL with the expected
and actual values, and the run ends with a total such as `24/24 passed`.
It uses temporary files, so it never touches your real `tickets.txt`. The
program exits with code 1 if any check fails.

## Learning demos

Smaller programs from earlier steps can also be run on their own:

```
java -cp out Warmup
java -cp out com.opsdesk.tickets.OopDemo
java -cp out com.opsdesk.tickets.CollectionsDemo
java -cp out com.opsdesk.tickets.ModernJavaDemo
```

## How the code is organised

| Class | Job |
|---|---|
| `TicketCli` | The menu: reads input, prints results, loads on start and saves on quit |
| `TicketService` | The ticket logic: create, find, assign, change status, list, filter |
| `TicketFileStore` | Reading and writing `tickets.txt` |
| `TicketRepository` | Holds the tickets in memory and answers queries with streams |
| `Ticket` | Abstract base class: id, title, status, priority, assignee |
| `SupportTicket`, `AssetRequest` | The two kinds of ticket |
| `Assignable` | Interface for anything that can be assigned to a person |
| `TicketStatus`, `Priority` | Enums for the allowed values |
| `TicketSummary` | Record holding a short, read-only view of a ticket |
| `TicketNotFoundException` | Thrown when a ticket id does not exist |
| `TicketDemo` | Checks that everything above works |

## Design note: where composition beats inheritance

`AssetRequest` stores the requested asset as a single `assetName` string.
Real assets have more detail: a name, a serial number, a cost and a
warranty date. It would be tempting to make `AssetRequest` extend an
`Asset` class to pick up those fields, but a request is not an asset; it
*asks for* one. The better design is composition: give `AssetRequest` an
`Asset` field ("an asset request **has an** asset"). That keeps `Ticket`
as its only parent, lets the same `Asset` class be reused elsewhere (for
example an inventory list), and means changes to `Asset` don't ripple
through the ticket class hierarchy.
