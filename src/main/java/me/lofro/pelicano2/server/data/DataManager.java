package me.lofro.pelicano2.server.data;

import lombok.Getter;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.global.utils.Instantiable;
import me.lofro.pelicano2.server.ModServer;
import net.minecraft.server.MinecraftServer;

@Getter
public class DataManager extends Instantiable<ModServer> {

    private final JsonConfig missionData;
    private final JsonConfig skillData;
    private final JsonConfig workbenchData;
    private final JsonConfig updatesData;
    
    public DataManager(ModServer instance) throws Exception {
        super(instance);

        MinecraftServer minecraftServer = instance.minecraftServer();
        
        final String folderName = "pelicanoData";

        this.missionData = (minecraftServer.isDedicated()) ? JsonConfig.serverConfig("missionData.json", folderName, minecraftServer) : JsonConfig.savesConfig("missionData.json", folderName, minecraftServer);
        this.skillData = (minecraftServer.isDedicated()) ? JsonConfig.serverConfig("skillData.json", folderName, minecraftServer) : JsonConfig.savesConfig("skillData.json", folderName, minecraftServer);
        this.workbenchData = (minecraftServer.isDedicated()) ? JsonConfig.serverConfig("workbenchData.json", folderName, minecraftServer) : JsonConfig.savesConfig("workbenchData.json", folderName, minecraftServer);
        this.updatesData = (minecraftServer.isDedicated()) ? JsonConfig.serverConfig("updatesData.json", folderName, minecraftServer) : JsonConfig.savesConfig("updatesData.json", folderName, minecraftServer);
    }

    public void save() {
        instance().getMissionManager().save(missionData);
        instance().getSkillTreeManager().save(skillData);
        instance().getWorkBenchManager().save(workbenchData);
        instance().getUpdatesManager().save(updatesData);
    }

}
