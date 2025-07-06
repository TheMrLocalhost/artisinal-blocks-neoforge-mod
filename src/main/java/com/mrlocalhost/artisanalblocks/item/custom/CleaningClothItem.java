package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;

public class CleaningClothItem extends Item {

    public CleaningClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(blockPos).getBlock();

        if (!(level instanceof ServerLevel)) { // skip clientSide
            return InteractionResult.PASS;
        }

        if (clickedBlock == ModBlocks.ARTISANAL_BLOCK.get()) {
            level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
            level.playSound(null, blockPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }

}
