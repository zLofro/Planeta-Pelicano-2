package me.lofro.pelicano2.server.networking;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ServerReceivers {

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.ACCEPT_MISSION_C2S,
                (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
                    ServerPlayerEntity player = Main.getInstance().serverManager().minecraftServer().getPlayerManager().getPlayer(packetByteBuf.readUuid());

                    if (player != null) {
                        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

                        Missions mission = packetByteBuf.readEnumConstant(Missions.class);

                        if (mission != null) {
                            missionManager.assign(mission, player.getUuid());

                            player.sendMessage(Text.of("Misión " + mission.getTitle().getString() + " aceptada."));
                        }
                    }
                });
    }

}
