package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PassivePassageDust extends Item {

    public PassivePassageDust(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay,
            @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.passive_passage_dust.shift_down","\n").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.passive_passage_dust").withStyle(ChatFormatting.YELLOW));
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
            BlockState baseState = level.getBlockState(blockPos);
            boolean currPassibility = baseState.getValue(ArtisanalBlock.PASSIVE_PASSIBLE);
            level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ArtisanalBlock.PASSIVE_PASSIBLE, !currPassibility));
        }
        return InteractionResult.SUCCESS_SERVER;
    }

}
