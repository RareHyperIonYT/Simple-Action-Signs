package me.rarehyperion.sas.models;

import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class SignAction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String owner;

    private final String command;
    private final String description;
    private final int cost;

    public SignAction(final Player owner, final String command, final String description, final int cost) {
        this.owner = owner.getUniqueId().toString();
        this.command = command;
        this.description = description;
        this.cost = cost;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return String.format("SignAction{command='%s', cost=%d}", this.command, this.cost);
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;

        final SignAction that = (SignAction) obj;
        return this.cost == that.cost && Objects.equals(this.command, that.command);
    }

    @Override
    public int hashCode() {
        int result = this.command != null ? this.command.hashCode() : 0;
        result = 31 * result + this.cost;
        return result;
    }

}
