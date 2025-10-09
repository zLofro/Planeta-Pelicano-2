package me.lofro.pelicano2.server.skills.data;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class SkillData {

    private final Map<UUID, Map<Skills, Integer>> playerSkills;

    private final Map<UUID, Integer> upgradePoints;

    public SkillData(Map<UUID, Map<Skills, Integer>> playerSkills, Map<UUID, Integer> upgradePoints) {
        this.playerSkills = playerSkills;
        this.upgradePoints = upgradePoints;
    }

    public SkillData() {
        this(new HashMap<>(), new HashMap<>());
    }

}
