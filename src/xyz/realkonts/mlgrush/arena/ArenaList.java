package xyz.realkonts.mlgrush.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ArenaList {

    private static HashMap<UUID, Arena> inarena = new HashMap<>();

    private static ArrayList<UUID> inqueue = new ArrayList<>();

    public static HashMap<UUID, Arena> getInArena() {
        return inarena;
    }

    public static ArrayList<UUID> getInQueue() {
        return inqueue;
    }
}
