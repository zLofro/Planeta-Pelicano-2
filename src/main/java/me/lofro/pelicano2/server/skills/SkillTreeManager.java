package me.lofro.pelicano2.server.skills;

import lombok.Getter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.data.api.Restorable;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.skills.data.SkillData;
import me.lofro.pelicano2.server.skills.data.Skills;

import java.util.HashMap;
import java.util.UUID;

public class SkillTreeManager implements Restorable {

    private @Getter SkillData skillData;

    private final int MAX_LEVEL = 16;

    public SkillTreeManager(ModServer modServer) {
        restore(modServer.getDataManager().getMissionData());
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.skillData = new SkillData();
        } else {
            this.skillData = Main.gson().fromJson(jsonConfig.getJsonObject(), SkillData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(Main.gson().toJsonTree(skillData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSkillLevel(UUID player, Skills skill, int level) {
        if (!skillData.getPlayerSkills().containsKey(player)) {
            skillData.getPlayerSkills().put(player, new HashMap<>());
        }

        skillData.getPlayerSkills().get(player).put(skill, Math.min(level, MAX_LEVEL));
    }

    public int getSkillLevel(UUID player, Skills skill) {
        if (!skillData.getPlayerSkills().containsKey(player)) {
            skillData.getPlayerSkills().put(player, new HashMap<>());
        }

        if (!skillData.getPlayerSkills().get(player).containsKey(skill)) {
            skillData.getPlayerSkills().get(player).put(skill, 0);
        }

        return Math.min(skillData.getPlayerSkills().get(player).get(skill), MAX_LEVEL);
    }

    public int getUpgradePoints(UUID player) {
        if (!skillData.getUpgradePoints().containsKey(player)) {
            skillData.getUpgradePoints().put(player, 0);
        }

        return skillData.getUpgradePoints().get(player);
    }

    public void addUpgradePoints(UUID player, int points) {
        setUpgradePoints(player, getUpgradePoints(player) + points);
    }

    public void setUpgradePoints(UUID player, int points) {
        skillData.getUpgradePoints().put(player, points);
    }

}
