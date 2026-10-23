# ticket-cli

A command-line ticket tool written in plain Java 25, built as a terminal
companion to the OpsDesk help-desk project. It is being built step by step:
Java basics, object-oriented design, collections, and finally an interactive
ticket CLI.

## Requirements

- Java 25 (JDK), with `java` and `javac` on your PATH

## Compile and run

From the project root (works in PowerShell and Git Bash):

```
javac -d out src/*.java src/com/opsdesk/tickets/*.java
```

Start the interactive ticket CLI:

```
java -cp out com.opsdesk.tickets.TicketCli
```

The menu offers 1) Create, 2) Assign, 3) Status, 4) List, 5) Filter and
0) Quit. Create, List and Quit work today; the others are coming next.

The learning demos can also be run on their own:

```
java -cp out Warmup
java -cp out com.opsdesk.tickets.OopDemo
java -cp out com.opsdesk.tickets.CollectionsDemo
java -cp out com.opsdesk.tickets.ModernJavaDemo
```

Compiled `.class` files go into `out/`, which is not committed.

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
