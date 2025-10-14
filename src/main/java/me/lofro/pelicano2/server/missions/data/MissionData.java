package me.lofro.pelicano2.server.missions.data;

import lombok.Getter;
import me.lofro.pelicano2.server.missions.missions.Missions;

import java.util.*;

@Getter
public class MissionData {

    private final Map<UUID, List<Missions>> completedMissions; // UUID del jugador, lista de misiones completadas
    private final Map<UUID, List<Missions>> activeMissions; // UUID del jugador, lista de misiones pendientes ACTIVAS

    public MissionData(Map<UUID, List<Missions>> completedMissions, Map<UUID, List<Missions>> activeMissions) {
        this.completedMissions = completedMissions;
        this.activeMissions = activeMissions;
    }

    public MissionData() {
        this.completedMissions = new HashMap<>();
        this.activeMissions = new HashMap<>();
    }

}
