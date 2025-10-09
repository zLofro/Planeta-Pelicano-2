package me.lofro.pelicano2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.lofro.pelicano2.global.items.ModItems;
import me.lofro.pelicano2.server.audio.soundevents.PelicanoSoundEvents;
import me.lofro.pelicano2.server.commands.CommandRegistries;
import me.lofro.pelicano2.server.commands.arguments.AudioArgumentType;
import me.lofro.pelicano2.server.commands.arguments.MissionArgumentType;
import me.lofro.pelicano2.server.commands.arguments.SkillsArgumentType;
import me.lofro.pelicano2.server.entity.EntityRegistries;
import me.lofro.pelicano2.server.networking.ServerReceivers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.global.data.adapters.LocalDateSerializer;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.ModServer;

import java.time.LocalDate;

public class Main implements ModInitializer {

    private static @Getter Main instance;

    private static final @Getter Logger logger = LoggerFactory.getLogger(ModConstants.MOD_ID);

    private ModServer modServer;
    private ModClient modClient;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @Override
    public void onInitialize() {
        instance = this;

        this.modServer = new ModServer(this);
        this.modClient = new ModClient();

        PelicanoSoundEvents.register();

        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(ModConstants.MOD_ID, "audios"),
                AudioArgumentType.class,
                ConstantArgumentSerializer.of(AudioArgumentType::audio)
        );

        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(ModConstants.MOD_ID, "missions"),
                MissionArgumentType.class,
                ConstantArgumentSerializer.of(MissionArgumentType::mission)
        );

        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(ModConstants.MOD_ID, "skills"),
                SkillsArgumentType.class,
                ConstantArgumentSerializer.of(SkillsArgumentType::skill)
        );

        EntityRegistries.register();

        CommandRegistries.registerCommands();

        ServerReceivers.registerServerReceivers();

        ModItems.registerModItems();
    }

    /**
     * @return the Gson constant.
     */
    public static Gson gson() {
        return gson;
    }

    /**
     * @return the mod's server manager.
     */
    public ModServer serverManager() {
        return modServer;
    }

    /**
     * @return the mod's client manager.
     */
    public ModClient clientManager() {
        return modClient;
    }

}
