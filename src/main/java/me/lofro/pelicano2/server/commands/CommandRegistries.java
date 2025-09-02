package me.lofro.pelicano2.server.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandRegistries {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(PelicanoCommand::register);
    }

}
