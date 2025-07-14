package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class CleaningClothItem extends Item {

    public CleaningClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
    @NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay,
    @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.cleaning_cloth_item.shift_down_1").withStyle(ChatFormatting.AQUA));
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.cleaning_cloth_item.shift_down_2").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.cleaning_cloth_item").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(blockPos).getBlock();
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();

        if (!(level instanceof ServerLevel) || hand.equals(InteractionHand.OFF_HAND) || player == null) { // skip clientSide and off-hand
            return InteractionResult.PASS;
        }
        if (clickedBlock == ModBlocks.ARTISANAL_BLOCK.get() && player.isCrouching()) {
            level.setBlockAndUpdate(blockPos, ModBlocks.ARTISANAL_BLOCK.get().defaultBlockState());
            if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
                artisanalBlockEntity.drops(); //clear slots
            }
            level.playSound(null, blockPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);
        } else {
            return super.onItemUseFirst(stack, context);
        }
        return InteractionResult.SUCCESS_SERVER;
    }

}
