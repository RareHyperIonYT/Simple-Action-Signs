package me.rarehyperion.sas.actions;

import org.bukkit.entity.Player;

public abstract class BuiltInAction {

    public final String name;

    public BuiltInAction(final String name) {
        this.name = name.toLowerCase();
    }

    public abstract void execute(final Player player, final String[] args);

}
