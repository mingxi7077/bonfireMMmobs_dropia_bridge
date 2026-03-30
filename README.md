# bonfireMMmobs_dropia_bridge

![License](https://img.shields.io/badge/license-GPL--3.0-blue)
![Platform](https://img.shields.io/badge/platform-Paper%201.21.8-brightgreen)
![Dependencies](https://img.shields.io/badge/dependencies-MythicMobs%20%2B%20ItemsAdder-blueviolet)
![Status](https://img.shields.io/badge/status-active-success)

`bonfireMMmobs_dropia_bridge` is a ground-drop bridge that turns MythicMobs kill events into ItemsAdder-backed loot drops for the Bonfire content pipeline.

## Highlights

- Hooks MythicMobs death events and resolves Bonfire loot rules.
- Spawns ItemsAdder-backed drops through a dedicated bridge layer.
- Keeps the drop logic configurable and separate from mob definition files.
- Provides admin control through the `/mmdropia` command.

## Core Command

- `/mmdropia <reload|spawn>`

## Build

```powershell
.\mvnw.cmd -q -DskipTests package
```

## Repository Scope

- Source and config only.
- Build outputs and deployment artifacts are excluded from Git.

## License

GPL-3.0
