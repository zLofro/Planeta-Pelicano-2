package me.lofro.pelicano2.server.updates;

import lombok.Getter;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Getter
public enum Updates {

    ZOMBIES_INMUNES_SOL(Text.of("Ya no se queman al amanecer. Pueden caminar libremente bajo el sol y, como si fuera poco, golpean con un 15% extra de fuerza. Los días ya no son seguros."),
            Text.of("LOS ZOMBIES SON INMUNES A LA LUZ DEL SOL"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "zombis_inmunes_sol"))),
    CALAMARES_HOSTILES(Text.of("Dejan de ser criaturas pasivas. Ahora atacan al jugador en cuanto lo detectan y aplican veneno, haciendo del agua un lugar peligroso."),
            Text.of("LOS CALAMARES SE VUELVEN HOSTILES"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "calamares_hostiles"))),
    IRON_GOLEM_HOSTILES(Text.of("Ya no protegen aldeas. Son enemigos por naturaleza. Cualquier acercamiento puede ser tu sentencia."),
            Text.of("AHORA LOS IRON GOLEMS SON HOSTILES POR NATURALEZA."), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "iron_golems_hostiles"))),
    ILLAGERS_FUEGO(Text.of("Los Illagers evolucionaron. Disparan esferas de fuego y llaman refuerzos al combate. Enfrentarse a uno puede convertirse en una invasión."),
            Text.of("LOS ILLAGERS LANZAN FUEGO E INVOCA MONSTRUOS"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "illagers_mejorados"))),
    CAIDA_ITEMS(Text.of("Cada vez que recibes daño hay un 1% de probabilidad de que un objeto de tu mochila caiga al suelo. Cualquier golpe puede hacerte perder algo valioso."),
            Text.of("CAIDA DE ITEMS AL RECIBIR DAÑO"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "caida_items_danio"))),
    HOSTILE_SALMONS(Text.of("Matar un salmón desencadena su venganza. Una horda de salmones hostiles aparece para atacarte. Pescar ya no es una opción tranquila."),
            Text.of("EL SALMÓN RECLUTA UNA HORDA"), null),
    HAMBRE_BAJO_AGUA(Text.of("Mientras estés sumergido, tu barra de hambre disminuirá un 40% más rápido. Bucear demasiado tiempo se vuelve un riesgo mortal."),
            Text.of("BAJO EL AGUA EL HAMBRE AUMENTA"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "hambre_bajo_agua"))),
    ALDEANOS_COLOMBIANOS(Text.of("Los aldeanos adoptan “costumbres locales”: tienen un 30% de probabilidad de estafarte, vendiéndote basura por precios absurdos. Comercio bajo tu propio riesgo."),
            Text.of("ALDEANOS COLOMBIANOS (Y ESTAFADORES)"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "aldeanos_colombianos"))),
    SUENIO_DESORDENADO(Text.of("Dormir ya no garantiza descanso. Hay un 30% de probabilidad de que tu inventario se revuelva completamente al despertar."),
            Text.of("SUEÑO DESORDENADO"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "suenio_desordenado"))),
    ARANIAS_MULTIPLICAN(Text.of("Cada vez que matas una, aparecen tres más pequeñas. El exterminio se convierte en multiplicación."),
            Text.of("LAS ARAÑAS SE MULTIPLICAN"), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "aranias_multiplican")))
    ;

    final Text description;
    final Text summary;
    final SoundEvent sound;
    Updates(Text description, Text summary, SoundEvent soundEvent) {
        this.description = description;
        this.summary = summary;
        this.sound = soundEvent;
    }

}
