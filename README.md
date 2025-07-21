# Simple Action Signs
A powerful lightweight Minecraft plugin that allows players to create interactive action signs with optional cost requirements.

## Features
- **Interactive Action Signs**: Create signs that execute commands when right-clicked.
- **Cost System**: Optional economy integration with customizable costs.


## How to use
### Creating Action Signs
1. Place any sign in the world.
2. Write the sign text in this format:
    ```
    [title]        (required)
    <command>      (required)
    <description>  (optional)
    cost           (optional)
    ```
   
### Example Sign Setup
```
[teleport]
/spawn
Teleport to spawn
100
```

### Example with placeholders
```
[idiot]
say I, <player>, am an idiot.
Insult yourself.
```

## Commands
- `/sas reload` - Reload the plugin configuration
  - Permission: `sas.reload`

## Permissions
| Permission     | Description                        | Default |
|----------------|------------------------------------|---------|
| `sas.create`   | Create and destroy command signs   | op      |
| `sas.use`      | Use/interact with command signs    | op      |
| `sas.reload`   | Reload plugin configuration        | op      |


## Configuration
<details>
  <summary>config.yml</summary>

  ```yml
# Currency symbol to display before costs on signs.
currency-prefix: "&a$"

# Command to execute when charging players for sign usage.
# Placeholders: <player> = player name, <cost> = amount to charge.
cost-command: "eco take <player> <cost>"

# Plugin messages.
messages:
  action-create: "&aAction sign successfully created!"
  action-delete: "&cAction sign successfully removed!"
  sneak-required: "&cYou need to sneak to do this."
  permission-use: "&cYou do not have permission to do this."
  ```

</details>