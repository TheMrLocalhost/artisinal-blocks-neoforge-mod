package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class CleaningClothItem extends Item {

    public CleaningClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
    @NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay,
    @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.cleaning_cloth_item.shift_down","\n").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.cleaning_cloth_item").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(blockPos).getBlock();
        InteractionHand hand = context.getHand();

        if (!(level instanceof ServerLevel) || hand.equals(InteractionHand.OFF_HAND)) { // skip clientSide and off-hand
            return InteractionResult.PASS;
        }

        if (clickedBlock == ModBlocks.ARTISANAL_BLOCK.get()) {
            level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
            level.playSound(null, blockPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS_SERVER;
    }

}
