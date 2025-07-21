package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@SuppressWarnings("deprecation")
public class ArtisanalChiselItem extends Item {

    public ArtisanalChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
    @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.artisanal_chisel_item.shift_down_1").withStyle(ChatFormatting.AQUA));
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.artisanal_chisel_item.shift_down_2").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.artisanal_chisel_item").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        if (!(level instanceof ServerLevel) || hand.equals(InteractionHand.OFF_HAND) || player == null) { // skip clientSide and off-hand
            return InteractionResult.PASS;
        }
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            if (!level.isClientSide()) {
                player.openMenu(
                    new SimpleMenuProvider(
                        artisanalBlockEntity,
                        Component.translatable("gui.artisanalblocks.artisanal_block.display_name")),
                    blockPos);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }

}
