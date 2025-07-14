package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class LightingRodItem extends Item {

    public LightingRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay,
            @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.lighting_rod_item.shift_down_1").withStyle(ChatFormatting.AQUA));
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.lighting_rod_item.shift_down_2").withStyle(ChatFormatting.AQUA));
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.lighting_rod_item.shift_down_3").withStyle(ChatFormatting.RED));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.artisanalblocks.lighting_rod_item").withStyle(ChatFormatting.YELLOW));
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

        //Client-Side
        if (!(level instanceof ServerLevel)) {
            if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
                int currentLightValue = artisanalBlockEntity.getBlockState().getValue(ArtisanalBlock.GLOW);
                int nextLightValue;
                if (!player.isCrouching()) {
                    nextLightValue = Integer.min(15, currentLightValue+1);

                } else {
                    nextLightValue = Integer.max(0, currentLightValue-1);
                }
                float pitch = (((float) nextLightValue) / 15.0F) + 0.5F;
                player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, pitch);
                player.displayClientMessage(Component.translatable("client_message.pop_up.artisanalblocks.light_level").append(String.valueOf(nextLightValue)).withStyle(ChatFormatting.YELLOW), true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        //Server-Side
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            int value;
            if (!player.isCrouching()) {
                value = Integer.min(artisanalBlockEntity.getBlockState().getValue(ArtisanalBlock.GLOW) + 1, 15);
            } else {
                value = Integer.max(artisanalBlockEntity.getBlockState().getValue(ArtisanalBlock.GLOW)-1,0);
            }
            level.setBlockAndUpdate(blockPos, artisanalBlockEntity.getBlockState().setValue(ArtisanalBlock.GLOW,value));
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS_SERVER;
    }

}
