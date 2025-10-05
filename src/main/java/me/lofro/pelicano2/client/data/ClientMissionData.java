package me.lofro.pelicano2.client.data;

import lombok.Getter;
import lombok.Setter;
import me.lofro.pelicano2.server.missions.missions.Missions;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientMissionData {

    private List<Missions> assignedMissions;

    public ClientMissionData() {
        this.assignedMissions = new ArrayList<>();
    }

}
