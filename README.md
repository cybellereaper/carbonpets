# CarbonPets

CarbonPets is a Paper/Spigot plugin that adds summonable companion pets with movement logic, visual effects, and talent-based combat support.

## Features

- **Summon/dismiss pet companions** with a simple `/pet` command.
- **Pet catalog system** for registering multiple pet definitions.
- **Talent engine** supporting:
  - passive interval talents,
  - random proc talents,
  - manual talents triggered by player interaction.
- **Cooldown and interval gates** to keep talent triggering balanced.
- **Entity controller and movement planner** so pets follow owners smoothly.

## Requirements

- **Java 25+** (required for Paper 26.1.1 builds).
- **Gradle** (or use the included Gradle wrapper once added).
- A **Paper/Spigot-compatible server** using the API version declared in `plugin.yml`.

## Build

From the repository root:

```bash
./gradlew clean build
```

If your environment does not include an executable wrapper script, use your local Gradle install:

```bash
gradle clean build
```

The plugin JAR will be generated in:

```text
build/libs/
```

## Install on a Server

1. Build the project.
2. Copy the generated JAR from `build/libs/` into your server's `plugins/` directory.
3. Start or restart the server.
4. Verify the plugin loads without errors in server logs.

## How to Use

### Basic Commands

- `/pet list`
  - Lists all available pet IDs you can summon.
- `/pet summon <id>`
  - Summons the pet matching `<id>`.
- `/pet dismiss`
  - Dismisses your currently active pet.

### Recommended First Run

1. Join the server as a player.
2. Run `/pet list` and pick one of the IDs shown.
3. Run `/pet summon <id>`.
4. Move around and interact with nearby mobs to observe movement + talent behavior.
5. Run `/pet dismiss` when done.

## Permissions

- `petplugin.command.pet`
  - Default: `true`
  - Allows use of all `/pet` subcommands.

## Project Structure (High-level)

```text
src/main/java/com/github/cybellereaper/carbon/
  Carbon.java                         # Plugin bootstrap/wiring
  pets/
    PetService.java                   # Pet lifecycle management
    PetEntityController.java          # Runtime control of pet entities
    PetMovementPlanner.java           # Movement decisions
    TalentEngine.java                 # Talent scheduling/execution
    talents/                          # Concrete talent implementations
src/main/resources/
  plugin.yml                          # Bukkit/Paper plugin metadata
```

## Troubleshooting

- **`Unknown pet. Use /pet list.`**
  - The provided pet ID is not in the active catalog.
- **`Only players can use pet commands.`**
  - `/pet` cannot be used from console or command blocks.
- **Command not recognized**
  - Confirm plugin startup succeeded and `plugin.yml` is packaged correctly.

## Development Notes

- Command handling lives in `PetCommand`.
- Prefer extending the `pets/talents` package for new talent behaviors.
- Keep new systems injectable via constructors to preserve testability and clean wiring.
