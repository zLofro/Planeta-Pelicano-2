package me.lofro.pelicano2.server.workbench;

import lombok.Getter;
import me.lofro.pelicano2.global.utils.OtherModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

@Getter
public enum WorkBenchRecipe {

    FIRE_GAUNTLET(new Pair[]{new Pair<>(Items.IRON_INGOT, 64), new Pair<>(Items.IRON_INGOT, 64), new Pair<>(Items.FLINT_AND_STEEL, 1),
            new Pair<>(Items.FIRE_CHARGE, 10)}, OtherModUtils.getOtherModItem("artifacts", "fire_gauntlet"), 5400),
    SHOCK_PENDANT(new Pair[]{new Pair<>(OtherModUtils.getOtherModItem("ribbits", "maraca"), 1), new Pair<>(Items.LIGHTNING_ROD, 10),
            new Pair<>(Items.BLUE_DYE, 64), new Pair<>(Items.OBSERVER, 1), new Pair<>(Items.FERMENTED_SPIDER_EYE, 64)},
            OtherModUtils.getOtherModItem("artifacts", "shock_pendant"), 7200),
    SNORKEL(new Pair[]{new Pair<>(Items.KELP, 64), new Pair<>(Items.KELP, 64), new Pair<>(Items.AMETHYST_SHARD, 64), new Pair<>(Items.REDSTONE, 64),
            new Pair<>(Items.PUFFERFISH, 3)}, OtherModUtils.getOtherModItem("artifacts", "snorkel"), 5400),
    CRYSTAL_HEART(new Pair[]{new Pair<>(Items.GOLDEN_APPLE, 32), new Pair<>(Items.CONDUIT, 1), new Pair<>(OtherModUtils.getOtherModItem("deeperdarker", "heart_of_the_deep"), 1),
            new Pair<>(OtherModUtils.getOtherModItem("the_bumblezone", "sugar_water_bucket"), 1)},
            OtherModUtils.getOtherModItem("artifacts", "crystal_heart"), 7200),
    CLOUD_IN_A_BOTTLE(new Pair[]{new Pair<>(Items.POWDER_SNOW_BUCKET, 1), new Pair<>(Items.GOLD_INGOT, 32),
            new Pair<>(OtherModUtils.getOtherModItem("inmersive_melodies", "triangle"), 1), new Pair<>(Items.FEATHER, 32),
            new Pair<>(Items.ECHO_SHARD, 10)},
            OtherModUtils.getOtherModItem("artifacts", "cloud_in_a_bottle"), 5400)
    ;

    final Pair<Item, Integer>[] recipe;
    final Item result;
    final int seconds;
    WorkBenchRecipe(Pair<Item, Integer>[] recipe, Item result, int seconds) {
        this.recipe = recipe;
        this.result = result;
        this.seconds = seconds;
    }

}
