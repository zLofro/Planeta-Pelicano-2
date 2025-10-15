package me.lofro.pelicano2.server.missions.missions;

import lombok.Getter;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

@Getter
public enum Missions {
    UN_LUGAR_PARA_DESCANSAR(List.of(Items.CAKE), Text.of("Un lugar para descansar"), Text.of("""
            Un héroe siempre necesita un refugio seguro, un lugar para guardar lo valioso y descansar sin temor. Sin hogar, hasta el más fuerte se pierde.
            
            Construye tu propia casa y asegúrate de que esté lista para habitar.
            
            Al regresar conmigo, recibirás una valiosa recompensa por haber levantado tu refugio.
            
            Recompensa: \uE101 \uE106 \uE107
            """),
            false,
            new Identifier(ModConstants.MOD_ID, "guide"),
            Text.literal("Construye tu propia casa y asegúrate de que esté habitable.").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE101 \uE106 \uE107").formatted(Formatting.WHITE))),
    EL_SECRETO_DE_LAS_MARIPOSAS(List.of(Items.CAKE), Text.of("El secreto de las mariposas"), Text.of("""
            Desde hace tiempo colecciono mariposas; para mí son tesoros vivos. Cada una, con sus colores y formas únicas, guarda un secreto de la naturaleza.
            
            Viaja a los campos, atrapa algunas mariposas con una red y tráemelas.
            
            Recibirás un regalo especial, algo que puede serte muy útil en tu viaje.
            
            Recompensa: \uE106 \uE100
            """),
            false,
            new Identifier(ModConstants.MOD_ID, "nicte"),
            Text.literal("Viaja a los campos y captura 30 mariposas con la red.").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE106 \uE100").formatted(Formatting.WHITE))),
    ALAS_DE_SOMBRA(List.of(Items.CAKE), Text.of("Alas de sombra"), Text.literal("""
            Un vecino lejano está siendo atacado por buitres hambrientos. Su casa, que alguna vez fue tranquila, ahora está rodeada por estas aves que no le dan descanso.
            
            Elimina a todos los buitres en esa zona y asegura la casa del vecino.
            
            Al volver conmigo, obtendrás un obsequio en agradecimiento por tu ayuda.
            
            Recompensa: \uE109
            """),
            true,
            new Identifier(ModConstants.MOD_ID, "textures/entity/npc/anastasia_white.png"),
            Text.literal("Elimina 20 buitres de la zona.").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE109").formatted(Formatting.WHITE))
            );

    private final List<Item> reward;
    private final Text title;
    private final Text description;
    private final boolean isNPC;
    private final Identifier npcTexture;
    private final Text summary;

    Missions(List<Item> reward, Text title, Text description, boolean isNPC, Identifier npcTexture, Text summary) {
        this.reward = reward;
        this.title = title;
        this.description = description;
        this.isNPC = isNPC;
        this.npcTexture = npcTexture;
        this.summary = summary;
    }

}
