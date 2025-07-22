# Simple Action Signs
A powerful lightweight Minecraft plugin that allows players to create interactive action signs with optional cost requirements.

## How to use
### Creating Action Signs
1. Place any sign in the world.
2. Write the sign text in this format:
```
[title]        (required)
<command>      (required)
<description>  (optional)
<cost / desc>  (optional)
```

### Example Sign Setup
```
[teleport]
/spawn
Teleport to spawn
100
```

## Permissions
| Permission     | Description                        | Default |
|----------------|------------------------------------|---------|
| `sas.create`   | Create and destroy command signs   | op      |
| `sas.use`      | Use/interact with command signs    | op      |
| `sas.reload`   | Reload plugin configuration        | op      |

## Placeholders
| Placeholder      | Description                           |
|------------------|---------------------------------------|
| `@p`             | The players name.                     |
| `@u`             | The players UUID.                     |
| `@w`             | The world the player is currently in. |
| `@x`, `@y`, `@z` | The players current position.         |

## Commands
- `/sas reload` - Reload the plugin configuration
    - Permission: `sas.reload`

## Configuration
<details>
  <summary>config.yml</summary>

  ```yml
  economy:
    enabled: true
    prefix: "&a$"
  
  # Plugin messages.
  messages:
    not-enough-money: "&cYou do not have enough money to do this."
    reload-success: "&aConfiguration reloaded!"
    action-create: "&aAction sign successfully created!"
    action-delete: "&cAction sign successfully removed!"
    sneak-required: "&cYou need to sneak to do this."
    permission-use: "&cYou do not have permission to do this."
    action-not-found: "&cThe action '<name>' does not exist. Did you mean to use a slash command?"
  ```

</details>