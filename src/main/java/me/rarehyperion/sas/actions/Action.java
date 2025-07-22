package me.rarehyperion.sas.actions;

import org.bukkit.entity.Player;

public abstract class Action {

    public final String name;

    public Action(final String name) {
        this.name = name.toLowerCase();
    }

    public abstract void execute(final Player player, final String[] args);

}
