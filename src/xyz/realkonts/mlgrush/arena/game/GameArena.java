package xyz.realkonts.mlgrush.arena.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

public class GameArena {

    @Getter
    private final String name;

    @Getter
    private final String worldName;
    @Getter
    private final Location blueSpawn;
    @Getter
    private final Location blueBed;
    @Getter
    private final Location redSpawn;
    @Getter
    private final Location redBed;
    @Getter
    private final Location pos1;
    @Getter
    private final Location pos2;

    @Getter
    @Setter
    private boolean available = true;

    @Getter
    private final Set<UUID> players = new HashSet<>();



    public GameArena(String name, String worldName, Location blueSpawn, Location blueBed,
                     Location redSpawn, Location redBed, Location pos1, Location pos2) {
        this.name = name;
        this.worldName = worldName;
        this.blueSpawn = blueSpawn;
        this.blueBed = blueBed;
        this.redSpawn = redSpawn;
        this.redBed = redBed;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Set<UUID> getPlayers() {
        return players;
    }
}
