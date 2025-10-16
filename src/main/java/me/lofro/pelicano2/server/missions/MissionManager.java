package me.lofro.pelicano2.server.missions;

import lombok.Getter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.data.api.Restorable;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.missions.data.MissionData;
import me.lofro.pelicano2.server.entity.ModEntities;
import me.lofro.pelicano2.server.entity.entities.GuideMissionsEntity;
import me.lofro.pelicano2.server.entity.entities.NPCEntity;
import me.lofro.pelicano2.server.entity.entities.NicteMissionsEntity;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MissionManager implements Restorable {

    private final ModServer modServer;

    private @Getter MissionData missionData;

    public MissionManager(final ModServer modServer) {
        this.modServer = modServer;

        this.restore(modServer.getDataManager().getMissionData());
    }

    public void openViewScreen(ServerPlayerEntity player) {
        Map<Missions, Boolean> playerMissions = new HashMap<>();

        if (!missionData.getActiveMissions().isEmpty() && missionData.getActiveMissions().get(player.getUuid()) != null) {
            missionData.getActiveMissions().get(player.getUuid()).forEach(mission -> {
                playerMissions.put(mission, false);
            });
        }

        if (!missionData.getCompletedMissions().isEmpty() && missionData.getCompletedMissions().get(player.getUuid()) != null) {
            missionData.getCompletedMissions().get(player.getUuid()).forEach(mission -> {
                playerMissions.put(mission, true);
            });
        }

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeMap(playerMissions, PacketByteBuf::writeEnumConstant, PacketByteBuf::writeBoolean);
        ServerPlayNetworking.send(player, PacketIDS.OPEN_MISSION_VIEW_SCREEN_S2C, buf);
    }

    public void openAssignScreen(ServerPlayerEntity player, Missions mission) {
        if (player != null) {
            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeEnumConstant(mission);

            ServerPlayNetworking.send(player, PacketIDS.OPEN_MISSION_SCREEN_S2C, buf);
        }
    }

    public void complete(Missions mission, ServerPlayerEntity player) {
        if (!completed(player.getUuid(), mission)) {
            this.missionData.getCompletedMissions().get(player.getUuid()).add(mission);

            removeActive(mission, player.getUuid());
        }

        player.sendMessage(Text.literal("¡Has completado exitosamente la misión " + mission.getTitle()).formatted(Formatting.GREEN));
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "complete_mission")), SoundCategory.MASTER, 1.0f, 1.0f);

        for (Pair<Item, Integer> reward : mission.getReward()) {
            if (reward.getLeft() != null) {
                player.giveItemStack(new ItemStack(reward.getLeft(), reward.getRight()));
                player.sendMessage(Text.literal("Has recibido x" + reward.getRight() + " " + reward.getLeft().getName().getString()).formatted(Formatting.AQUA));
            }
        }
    }

    public void removeActive(Missions mission, UUID playerUUID) {
        if (active(playerUUID, mission)) {
            this.missionData.getActiveMissions().get(playerUUID).remove(mission);
        }
    }

    public void removeCompleted(Missions mission, UUID playerUUID) {
        if (completed(playerUUID, mission)) {
            this.missionData.getCompletedMissions().get(playerUUID).remove(mission);
        }
    }

    public void assign(Missions mission, UUID playerUUID) {
        if (!active(playerUUID, mission)) {
            this.missionData.getActiveMissions().get(playerUUID).add(mission);
        }
    }

    public boolean completed(UUID playerUUID, Missions mission) {
        if (!this.missionData.getCompletedMissions().containsKey(playerUUID)) this.missionData.getCompletedMissions().put(playerUUID, new ArrayList<>());

        return missionData.getCompletedMissions().get(playerUUID).contains(mission);
    }

    public boolean active(UUID playerUUID, Missions mission) {
        if (!this.missionData.getActiveMissions().containsKey(playerUUID)) this.missionData.getActiveMissions().put(playerUUID, new ArrayList<>());

        return missionData.getActiveMissions().get(playerUUID).contains(mission);
    }

    public void spawnNPC(Missions mission, World world, Vec3d location) {
        if (mission.isNPC()) {
            NPCEntity npc = ModEntities.NPC_ENTITY.create(world);

            if (npc != null) {
                npc.setPosition(location);

                world.spawnEntity(npc);

                npc.setMission(mission);
            }
        } else {
            if (mission.getNpcTexture().getPath().equals("guide")) {
                GuideMissionsEntity guide = ModEntities.GUIDE_MISSIONS.create(world);

                if (guide != null) {
                    guide.setPosition(location);

                    world.spawnEntity(guide);

                    guide.setMission(mission);
                }
            } else {
                NicteMissionsEntity nicte = ModEntities.NICTE_MISSIONS.create(world);

                if (nicte != null) {
                    nicte.setPosition(location);

                    world.spawnEntity(nicte);

                    nicte.setMission(mission);
                }
            }
        }
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.missionData = new MissionData();
        } else {
            this.missionData = Main.gson().fromJson(jsonConfig.getJsonObject(), MissionData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(Main.gson().toJsonTree(missionData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
