package me.lofro.pelicano2.server.missions.missions;

import lombok.Getter;
import me.lofro.pelicano2.global.item.ModItems;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.global.utils.OtherModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

@Getter
public enum Missions {
    UN_LUGAR_PARA_DESCANSAR(new Pair[]{new Pair<>(OtherModUtils.getOtherModItem("dungeonnowloading", "great_experience_bottle"), 1),
            new Pair<>(OtherModUtils.getOtherModItem("artifacts", "power_glove"), 1), new Pair<>(OtherModUtils.getOtherModItem("inmersive_melodies", "bagpipe"), 1)},
            Text.of("Un lugar para descansar"), Text.of("""
            Un héroe siempre necesita un refugio seguro, un lugar para guardar lo valioso y descansar sin temor. Sin hogar, hasta el más fuerte se pierde.
            
            Construye tu propia casa y asegúrate de que esté lista para habitar.
            
            Al regresar conmigo, recibirás una valiosa recompensa por haber levantado tu refugio.
            
            Recompensa: \uE101 \uE106 \uE107
            """),
            false,
            new Identifier(ModConstants.MOD_ID, "guide"),
            Text.literal("Construye tu casa y hazla habitable").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE101 \uE106 \uE107").formatted(Formatting.WHITE))),
    DEFENDER_SYLMORIA(new Pair[]{new Pair<>(OtherModUtils.getOtherModItem("waystones", "waystone"), 1), new Pair<>(ModItems.HUMAN_UPGRADE_POTION, 2)},
            Text.of("Defender Sylmoria"), Text.of("""
            Tras meses de abandono, Sylmoria cayó en manos de enemigos y criaturas ocultas en la oscuridad. Nuestro hogar ahora es tierra hostil.
            
            Derrota a los enemigos y limpia el terreno para comenzar a recuperar el país.
            
            Recibirás objetos útiles que te ayudarán a moverte por el mapa.
            
            Recompensas: \uE110 \uE100
            """),
            false,
            new Identifier(ModConstants.MOD_ID, "guide"),
            Text.literal("Derrota a los enemigos").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE110 \uE100").formatted(Formatting.WHITE))),
    EL_SECRETO_DE_LAS_MARIPOSAS(new Pair[]{new Pair<>(OtherModUtils.getOtherModItem("dungeonnowloading", "great_experience_bottle"), 10),
            new Pair<>(ModItems.HUMAN_UPGRADE_POTION, 1)}
            , Text.of("El secreto de las mariposas"), Text.of("""
            Desde hace tiempo colecciono mariposas; para mí son tesoros vivos. Cada una, con sus colores y formas únicas, guarda un secreto de la naturaleza.
            
            Viaja a los campos, atrapa algunas mariposas con una red y tráemelas.
            
            Recibirás un regalo especial, algo que puede serte muy útil en tu viaje.
            
            Recompensa: \uE106 \uE100
            """),
            false,
            new Identifier(ModConstants.MOD_ID, "nicte"),
            Text.literal("Viaja a los campos y captura 30 mariposas con la red").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE106 \uE100").formatted(Formatting.WHITE))),
    LA_MELODIA_PERDIDA(new Pair[]{new Pair<>(Items.GOLDEN_APPLE, 10), new Pair<>(ModItems.HUMAN_UPGRADE_POTION, 1)}, Text.of("La melodía perdida"),
            Text.of("""
                    ¡No me hables ahora, estoy a punto de explotar! … Perdona, grité más de la cuenta. Esperaba un paquete con instrumentos hoy y lo retrasaron una semana. ¿Qué haré mientras tanto? Si pudieras ayudarme a reunir los de esta lista, te lo agradecería. Y tranquilo, te pagaré por ello.
                    
                    Consigue los instrumentos que aparecen en la lista y tráemelos a este mismo lugar.
                    
                    Te recompensaré con un premio fabuloso.
                    
                    Recompensa: \uE104 \uE100
                    """),
            false,
            new Identifier(ModConstants.MOD_ID, "nicte"),
            Text.literal("Consigue los instrumentos").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE104 \uE100").formatted(Formatting.WHITE))),
    ALAS_DE_SOMBRA(new Pair[]{new Pair<>(OtherModUtils.getOtherModItem("moretotems", "skeletal_totem_of_undying"), 1)}, Text.of("Alas de sombra"), Text.literal("""
            Un vecino lejano está siendo atacado por buitres hambrientos. Su casa, que alguna vez fue tranquila, ahora está rodeada por estas aves que no le dan descanso.
            
            Elimina a todos los buitres en esa zona y asegura la casa del vecino.
            
            Al volver conmigo, obtendrás un obsequio en agradecimiento por tu ayuda.
            
            Recompensa: \uE109
            """),
            true,
            new Identifier(ModConstants.MOD_ID, "textures/entity/npc/anastasia_white.png"),
            Text.literal("Elimina 20 buitres de la zona").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE109").formatted(Formatting.WHITE))),
    VESTIDOS_INCONCLUSOS(new Pair[]{new Pair<>(ModItems.HUMAN_UPGRADE_POTION, 1)}, Text.of("Vestidos inconclusos"), Text.of("""
            ¡Ah, eres tú! Qué alegría verte. Estaba tejiendo unos vestidos, pero me quedé sin hilo. Si pudieras ayudarme a conseguir más, recibirías… ¡puntos de habilidad!
            
            Consigue rollos de hilo y entrégamelos para que pueda terminar mis vestidos.
            
            Obtendrás puntos de habilidad como recompensa por tu ayuda.
            
            Recompensa: \uE100
            """),
             true,
            new Identifier(ModConstants.MOD_ID, "textures/entity/npc/anastasia_white.png"),
            Text.literal("Recolecta 64 de hilo").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE100").formatted(Formatting.WHITE))),
    DULCE_REMEDIO(new Pair[]{new Pair<>(Items.GOLDEN_CARROT, 5), new Pair<>(OtherModUtils.getOtherModItem("promenade", "cooked_duck"), 1)},
            Text.of("Dulce remedio"), Text.of("""
            Dicen que nada calma mejor las penas que la miel. Su sabor único me da la energía para seguir trabajando; sin ella, mis días se vuelven pesados.
            
            Consigue varias botellas de miel y tráemelas.
            
            Si cumples con la tarea, te daré un premio que hará que tu esfuerzo valga la pena.
            
            Recompensa: \uE105 \uE102
            """),
            true,
            new Identifier(ModConstants.MOD_ID, "textures/entity/npc/kianny.png"),
            Text.literal("Recolecta 20 botellas de miel").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE105 \uE102").formatted(Formatting.WHITE))),
    EL_ANTOJO_IMPOSIBLE(new Pair[]{new Pair<>(ModItems.HUMAN_UPGRADE_POTION, 1), new Pair<>(OtherModUtils.getOtherModItem("inmersive_melodies", "flute"), 1)},
            Text.of("El antojo imposible"), Text.of("""
            Desde esta mañana solo pienso en dulces. He buscado por todas partes y no queda ninguno. Si me traes algunos, sabré compensarte.
            
            Consigue una cantidad de dulces y entrégalos al personaje.
            
            Recibirás un obsequio especial como agradecimiento por calmar mis antojos.
            
            Recompensa: \uE100 \uE103
            """),
            true,
            new Identifier(ModConstants.MOD_ID, "textures/entity/npc/kianny.png"),
            Text.literal("Recolecta 64 dulces").styled(s -> s.withColor(0x444444)).append(Text.literal(" \uE100 \uE103").formatted(Formatting.WHITE)))
    ;

    private final Pair<Item, Integer>[] reward;
    private final Text title;
    private final Text description;
    private final boolean isNPC;
    private final Identifier npcTexture;
    private final Text summary;

    Missions(Pair<Item, Integer>[] reward, Text title, Text description, boolean isNPC, Identifier npcTexture, Text summary) {
        this.reward = reward;
        this.title = title;
        this.description = description;
        this.isNPC = isNPC;
        this.npcTexture = npcTexture;
        this.summary = summary;
    }

}
