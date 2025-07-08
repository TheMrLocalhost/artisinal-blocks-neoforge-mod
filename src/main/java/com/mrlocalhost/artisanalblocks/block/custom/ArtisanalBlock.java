package com.mrlocalhost.artisanalblocks.block.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ArtisanalBlock extends Block {

    public static final IntegerProperty GLOW = IntegerProperty.create("glow", 0, 15);

    public ArtisanalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(GLOW, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOW);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(
    @NotNull ItemStack stack, @NotNull BlockState state,
    @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
    @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            if (hand.equals(InteractionHand.MAIN_HAND) && player.getMainHandItem().is(Items.GLOWSTONE_DUST)) {
                level.setBlockAndUpdate(pos, state.setValue(GLOW,Integer.min(state.getValue(GLOW)+1,15)));
                return InteractionResult.SUCCESS_SERVER;
            } else if(hand.equals(InteractionHand.OFF_HAND) && player.getOffhandItem().is(Items.GLOWSTONE_DUST)) {
                level.setBlockAndUpdate(pos, state.setValue(GLOW,Integer.max(state.getValue(GLOW)-1,0)));
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.PASS;
        } else {
            if (player.getOffhandItem().isEmpty() && player.getMainHandItem().isEmpty()) {
                level.playSound(player, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                return InteractionResult.PASS;
            }
            return InteractionResult.SUCCESS;
        }
    }
}
