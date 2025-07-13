package com.mrlocalhost.artisanalblocks.utils;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;


public class ArtisanalBlocksUtilities {

    @SuppressWarnings("all") //Supressing usage of null when @NotNull is on isCollisionShapeFullBlock
    public static boolean isPlacableInArtisanalBlock(ItemStack item) {
        try {
            if (item.getItem().equals(ModBlocks.ARTISANAL_BLOCK.asItem())) {
                return true;
            }
            return (item.getItem() instanceof BlockItem blockItem)
                && !(blockItem.getBlock() instanceof BaseEntityBlock)
                && (blockItem.getBlock().defaultBlockState().isCollisionShapeFullBlock(null, null))
            ;
        } catch (Exception ignored) {
            return false;
        }
    }

}
