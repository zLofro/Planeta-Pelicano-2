package me.lofro.pelicano2.server.workbench;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

@Getter
public enum WorkBenchRecipe {

    TEST(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST1(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST2(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST3(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST4(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST5(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST6(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem()),
    TEST7(new Item[]{Items.ACACIA_BOAT.asItem(), Items.ACACIA_SIGN, Items.ACACIA_TRAPDOOR, Items.ACACIA_FENCE, Items.ACACIA_LOG.asItem()}, Items.ACACIA_LEAVES.asItem());

    final Item[] recipe;
    final Item result;
    WorkBenchRecipe(Item[] recipe, Item result) {
        this.recipe = recipe;
        this.result = result;
    }

}
