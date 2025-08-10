package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class HostileRodItem extends Item {

    public HostileRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
    @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.hostile_rod_item.shift_down_1").withStyle(ChatFormatting.AQUA));
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.hostile_rod_item.shift_down_2").withStyle(ChatFormatting.AQUA));
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.hostile_rod_item.shift_down_3").withStyle(ChatFormatting.RED));
        } else {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.hostile_rod_item").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();

        if (hand.equals(InteractionHand.OFF_HAND) || player == null) { // skip clientSide and off-hand
            return InteractionResult.PASS;
        }

        if (!(level instanceof ServerLevel)) {
            if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
                if (artisanalBlockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE).equals(ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED)) {
                    if (!player.isCrouching()) {
                        player.displayClientMessage(Component.translatable("client_message.pop_up.artisanalblocks.hostile_passible.true").withStyle(ChatFormatting.GREEN), true);
                        player.playNotifySound(SoundEvents.LEVER_CLICK, SoundSource.PLAYERS, 0.25F, 1.0F);
                    } else {
                        player.displayClientMessage(Component.translatable("client_message.pop_up.artisanalblocks.hostile_passible.false").withStyle(ChatFormatting.RED), true);
                        player.playNotifySound(SoundEvents.LEVER_CLICK, SoundSource.PLAYERS, 0.25F, 0.5F);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("client_message.pop_up.artisanalblocks.redstone_controlled").withStyle(ChatFormatting.RED), true);
                    player.playNotifySound(SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.PLAYERS, 0.25F, 0.5F);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            if (!player.isCrouching()) {
                level.setBlockAndUpdate(blockPos, artisanalBlockEntity.getBlockState().setValue(ArtisanalBlock.HOSTILE_PASSIBLE, true));
            } else {
                level.setBlockAndUpdate(blockPos, artisanalBlockEntity.getBlockState().setValue(ArtisanalBlock.HOSTILE_PASSIBLE, false));
            }
        } else {
            return super.onItemUseFirst(stack, context);
        }
        return InteractionResult.SUCCESS;
    }

}
