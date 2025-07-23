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
| Permission         | Description                            | Default |
|--------------------|----------------------------------------|---------|
| `sas.admin.reload` | Permission for reloading the plugin    | op      |
| `sas.admin`        | Create, edit, and destroy action signs | op      |
| `sas.use`          | Use/interact with action signs         | op      |

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
  
  language:
    reload: "&aConfiguration successfully reloaded!"
  
    sign-created: "&aAction Sign successfully created!"
    sign-deleted: "&cAction Sign successfully removed!"
    sign-edited: "&eAction Sign successfully edited!"
  
    sneak-required: "&cYou need to be sneaking to destroy action signs."
    permission-use: "&cYou do not have permission to do this."
    not-enough-money: "&cYou do not have enough money to do this."
    action-not-found: "&cThe action '<name>' does not exist. Did you mean to use a slash command?"
  
    list-signs:
      none-found: "&eNo signs were found."
      invalid-page: "&cThe page '<page>' doesn't exist. Maximum page is <totalPages>."
  
      title:
        - "&3=== &bSign Actions &7(Page <page>/<totalPages>) &3==="
        - "&7Total Signs: <totalSigns>"
      element-format:
        - "&3<index>. &b<key>"
        - "  &7Placed by: &f<name>"
        - "  &7Command: &f<command>"
        - "  &7Description: &f<description>"
        - "  &7Cost: <cost>"
      footer:
        - "&7Use &f/sas list <page> &7to navigate."
  ```

</details>