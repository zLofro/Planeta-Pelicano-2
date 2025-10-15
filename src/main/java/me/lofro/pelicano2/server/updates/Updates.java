package me.lofro.pelicano2.server.updates;

import lombok.Getter;
import net.minecraft.text.Text;

@Getter
public enum Updates {

    ZOMBIES_INMUNES_SOL(Text.of("Ya no se queman al amanecer. Pueden caminar libremente bajo el sol y, como si fuera poco, golpean con un 15% extra de fuerza. Los días ya no son seguros."), Text.of("LOS ZOMBIES SON INMUNES A LA LUZ DEL SOL")),
    CALAMARES_HOSTILES(Text.of("Dejan de ser criaturas pasivas. Ahora atacan al jugador en cuanto lo detectan y aplican veneno, haciendo del agua un lugar peligroso."), Text.of("LOS CALAMARES SE VUELVEN HOSTILES")),
    MARIPOSAS_HOSTILES(Text.of("Inofensivas a simple vista, pero ahora atacan y provocan mareo temporal. Una nube de estas puede dejarte completamente desorientado."), Text.of("LAS MARIPOSAS SON HOSTILES")),
    TEST3(Text.of("TEST TEST TEST TEST TEST TEST TEST TEST"), Text.of("Test")),
    TEST4(Text.of("TEST TEST TEST TEST TEST TEST TEST TEST :P"), Text.of("Test test test")),
    TEST5(Text.of("TEST TEST TEST TEST TEST TEST TEST TEST asd"), Text.of("Test prueba")),
    TEST6(Text.of("TEST TEST TEST TEST TEST TEST TEST TEST"), Text.of("Test test"));

    final Text description;
    final Text summary;
    Updates(Text description, Text summary) {
        this.description = description;
        this.summary = summary;
    }

}
