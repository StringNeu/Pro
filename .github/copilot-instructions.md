# Copilot Instructions

## Repository Overview

This is a minimal repository related to **KingIOServer** — an industrial IoT engineering design tool used in warehouse automation systems. The repository contains reference materials and logs for an AGV (Automated Guided Vehicle) TCP server integration.

## Repository Contents

| File | Description |
|------|-------------|
| `README.md` | Minimal readme placeholder |
| `LICENSE` | Repository license |
| `tcpserverlog.txt` | TCP server communication log showing AGV JSON messages (warehouse goods transport commands) |
| `001 培训表.xls` | Training table (Excel spreadsheet, in Chinese) |
| `KingIOServer工程设计器.lnk` | Windows shortcut to KingIOServer Engineering Designer |

## Key Facts

- **Language/Domain**: Industrial IoT, warehouse automation, AGV systems
- **Protocol**: TCP socket communication with JSON-encoded messages
- **Message format**: Binary-framed JSON payloads containing fields such as `messageId`, `agvMsg`, `jobCode`, `wareHouseCode`, `receiveAddress`, `roadwayCode`, `trayBarCode`, `deliveryAddress`, `type`, `billCode`, `mesNo`, `createTime`
- **No build system**: This repository has no build scripts, CI pipelines, tests, or package manager configuration files.

## Guidance for Coding Agents

- There is no build, lint, or test infrastructure in this repository — do not attempt to run build or test commands.
- If adding source code, follow the industrial IoT/AGV domain context (TCP server, JSON messaging, warehouse operations).
- The primary language in comments and filenames is Chinese (Simplified).
- Keep changes minimal and focused; avoid introducing unrelated dependencies or tooling.
