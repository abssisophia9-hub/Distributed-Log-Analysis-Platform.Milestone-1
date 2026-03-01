# DLAB – Distributed Log Analysis Platform
## Milestone 1: Client/Server Model

---

## Overview
DLAB is a distributed system that allows a client to perform **remote log file analysis** over a network. No libraries, no frameworks — everything is built from scratch.

---

## Architecture
```
[DlapClient.java]  ——— (port 8888) ———  [DlapServer.java]
  sends request header                               reads log file
  + filename                                         executes analysis
  receives response header                           sends response header back
```

---

## System Design

### Protocol

### Request Header Format (Client → Server)
```
COMMAND fileName\n
```

| Field | Description |
|---|---|
| `COMMAND` | The operation to perform (`COUNT` or `DIST`) |
| `fileName` | The name of the log file to analyse on the server |
| `\n` | Delimiter that synchronises the handshake |

### Response Header Format (Server → Client)
```
OK result\n
```
or for binary data:
```
OK size\n
<raw bytes body>
```
or on failure:
```
ERROR INVALID_COMMAND\n
```

---

## Operations

### COUNT
- **Request Header:** `COUNT fileName\n`
- **Response Header:** `OK <lineCount>\n`
- **Description:** Counts the total number of lines in the remote log file.
```
Client                            Server
  |                                 |
  |——— Request Header ————————————→ |
  |    "COUNT app.log\n"            |
  |                                 | reads file
  |                                 | counts lines
  |←—— Response Header ————————————|
  |    "OK 42\n"                    |
```

### DIST (Code Distribution)
- **Request Header:** `CODE DISTRIBUTION fileName\n`
- **Response Header:** `OK <size>\n` followed by raw bytes body
- **Description:** Returns the byte distribution of the remote log file.
```
Client                            Server
  |                                 |
  |——— Request Header ————————————→ |
  |    "CODE DISTRIBUTION app.log\n"|
  |                                 | reads file
  |                                 | builds distribution
  |←—— Response Header ————————————|
  |    "OK 256\n"                   |
  |←—— Body (raw bytes) ————————————|
  |    [12, 45, 0, ...]             |
```

---

## Implementation

| File | Role |
|---|---|
| `DlapServer.java` | Listens on port 8888, parses request header, analyses log file, sends response header |
| `DlapClient.java` | Connects to server, sends request header, reads response header, prints result |

---

## How to Run

### Step 1 — Compile
```bash
javac DlapServer.java
javac DlapClient.java
```

### Step 2 — Start the Server (Terminal 1)
```bash
java DlapServer
```
```
DLAP Server listening on port 8888...
```

### Step 3 — Run the Client (Terminal 2)

Count lines:
```bash
java DlapClient c app.log
```
```
The result of the count service requested is as follows: 42
```

Get distribution:
```bash
java DlapClient cd app.log
```
```
The code distribution is as follows: [12, 45, 0, ...]
```
