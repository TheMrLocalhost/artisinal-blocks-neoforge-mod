package com.mrlocalhost.artisanalblocks.utils;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.component.ModDataComponentTypes;
import com.mrlocalhost.artisanalblocks.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
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
                //uncomment to prevent transparent blocks (like glass, ice, and leaves)
                //&& !(blockItem.getBlock() instanceof HalfTransparentBlock)
                //&& !(blockItem.getBlock() instanceof LeavesBlock)
            ;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.EYEDROPPER.get(), ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID, "used"),
            (stack, level, entity, seed) ->
                (stack.has(DataComponents.CONTAINER)
                || stack.has(ModDataComponentTypes.LIGHT_LEVEL)
                || stack.has(ModDataComponentTypes.LIGHT_STATE)
                || stack.has(ModDataComponentTypes.PLAYER_STATE)
                || stack.has(ModDataComponentTypes.PASSIVE_STATE)
                || stack.has(ModDataComponentTypes.HOSTILE_STATE))
                ? 1.0F : 0.0F);
    }

}
