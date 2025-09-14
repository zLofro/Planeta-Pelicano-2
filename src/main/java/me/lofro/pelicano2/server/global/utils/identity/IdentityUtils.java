package me.lofro.pelicano2.server.global.utils.identity;

import me.lofro.pelicano2.server.global.utils.command.CommandUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class IdentityUtils {

    /**
     * Equipa una identidad a un jugador usando el mod Identity.
     *
     * @param player   El jugador al que se le aplicará la identidad
     * @param identityId  El ID de la entidad (ej: "minecraft:guardian", "pelicano2:custom_cat")
     * @param forceOp  Si true, ejecuta con nivel OP (ignora permisos del jugador)
     */
    public static void equipIdentity(ServerPlayerEntity player, String identityId, boolean forceOp) {
        String command = "identity equip @s " + identityId;
        CommandUtils.executeCommand(player, command, forceOp);
    }

}
