# Emeralds for Bukkit

> Emeralds-based Vault-compatible economy plugin

### Features

- Players can type `/$` or `/money` or `/emeralds` to open an inventory with all their emeralds.
- Players can have up to 31104 emeralds instantly available. If they have more, it will be hidden.
- Vault compatible, it provides a Vault-compatible economy and works with any economy plugin.
- Ultra lightweight, no Kotlin, no library, only a few kilobytes.
- Very safe to use, it can be used with real money in mind. Emeralds are locked when opening an inventory. Even if the
  server crashes, funds are still safe.

### Usage

Just type `/$` to open an inventory that represents your account, then put or take some emeralds.

You can use `/balance [player]` to check your (or another player) balance.

Admins can use `/givemoney <player> <amount>` or `/takemoney <player> <amount>` to give or take emeralds.