package me.lofro.pelicano2.server.workbench;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

@Getter
public enum WorkBenchRecipe {

    TEST(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 10),
    TEST1(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LOG.asItem(), 10),
    TEST2(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_CHEST_BOAT.asItem(), 20),
    TEST3(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 15),
    TEST4(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 30),
    TEST5(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 10),
    TEST6(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 5),
    TEST7(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem(), 4);

    final Item[] recipe;
    final Item result;
    final int seconds;
    WorkBenchRecipe(Item[] recipe, Item result, int seconds) {
        this.recipe = recipe;
        this.result = result;
        this.seconds = seconds;
    }

}
