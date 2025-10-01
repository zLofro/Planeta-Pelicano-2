package me.lofro.pelicano2.server.entity.entities;

import lombok.Getter;
import lombok.Setter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.UUID;

public class NPCEntity extends PathAwareEntity {
    private static final TrackedData<String> MISSION = DataTracker.registerData(NPCEntity.class, TrackedDataHandlerRegistry.STRING);

    private @Getter Identifier texture;

    private final Missions mission;

    public NPCEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.mission = getMission();
        this.texture = mission.getNpcTexture();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tickMovement() {
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();
            UUID playerUUID = player.getUuid();

            if (!missionManager.completed(playerUUID, mission) && !missionManager.active(playerUUID, mission)) {
                ServerPlayerEntity serverPlayerEntity = Main.getInstance().serverManager().minecraftServer().getPlayerManager().getPlayer(playerUUID);
                Main.getInstance().serverManager().getMissionManager().openAssignScreen(serverPlayerEntity, mission);
            } else {
                player.sendMessage(Text.of("¡Ya hablaste conmigo!"));
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MISSION, Missions.values()[0].name());
    }

    public void setMission(Missions mission) {
        this.dataTracker.set(MISSION, mission.name());
    }

    public Missions getMission() {
        return Missions.valueOf(this.dataTracker.get(MISSION));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Mission", this.getMission().name());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Mission")) {
            this.setMission(Missions.valueOf(nbt.getString("Mission")));
        }
    }

}
