package me.lofro.pelicano2.server.global.utils.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CommandUtils {

    /**
     * Ejecuta un comando en nombre de un jugador.
     *
     * @param player   El jugador que ejecutará el comando
     * @param command  El comando a ejecutar (puede empezar o no con "/")
     * @param forceOp  Si true, se ejecuta con nivel OP (4), aunque el jugador no lo sea
     */
    public static void executeCommand(ServerPlayerEntity player, String command, boolean forceOp) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandSource source = player.getCommandSource();
        if (forceOp) {
            source = source.withLevel(4);
        }

        server.getCommandManager().executeWithPrefix(source, command);
    }
}
