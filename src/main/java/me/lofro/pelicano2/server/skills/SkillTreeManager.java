package me.lofro.pelicano2.server.skills;

import lombok.Getter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.data.api.Restorable;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.skills.data.SkillData;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillTreeManager implements Restorable {

    private final ModServer modServer;

    private @Getter SkillData skillData;

    private final int MAX_LEVEL = 16;

    public SkillTreeManager(ModServer modServer) {
        this.modServer = modServer;

        restore(modServer.getDataManager().getSkillData());
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

    public Map<Skills, Integer> getSkills(UUID player) {
        Map<Skills, Integer> skills;
        if (skillData.getPlayerSkills().containsKey(player)) {
            skills = skillData.getPlayerSkills().get(player);

            boolean changed = false;

            for (Skills skill : Skills.values()) {
                if (!skills.containsKey(skill)) {
                    skills.put(skill, 0);
                    changed = true;
                }
            }

            if (changed) skillData.getPlayerSkills().put(player, skills);

        } else {
            skills = new HashMap<>();

            for (Skills skill : Skills.values()) {
                if (!skills.containsKey(skill)) {
                    skills.put(skill, 0);
                }
            }

            skillData.getPlayerSkills().put(player, skills);

        }
        return skills;
    }

    public void addSkillLevel(UUID uuid, Skills skills, int level) {
        updateSkillLevel(uuid, skills, getSkillLevel(uuid, skills) + level);
    }

    public void updateSkillLevel(UUID player, Skills skill, int level) {
        if (!skillData.getPlayerSkills().containsKey(player)) {
            skillData.getPlayerSkills().put(player, new HashMap<>());
        }

        int newLevel = Math.min(level, MAX_LEVEL);

        skillData.getPlayerSkills().get(player).put(skill, newLevel);
        onUpdateSkillLevel(modServer.minecraftServer().getPlayerManager().getPlayer(player), skill, newLevel);
    }

    public void onUpdateSkillLevel(ServerPlayerEntity player, Skills skill, int level) {
        if (skill == Skills.SPEED) {
            if (player != null) {
                double baseSpeed = 0.1D;
                double multiplier = 1.0D + (level / 10.0D);
                player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed * multiplier);
            }
        }
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

    public void removeUpgradePoints(UUID player, int points) {
        setUpgradePoints(player, getUpgradePoints(player) - points);
    }

    public void setUpgradePoints(UUID player, int points) {
        skillData.getUpgradePoints().put(player, points);
    }

}
