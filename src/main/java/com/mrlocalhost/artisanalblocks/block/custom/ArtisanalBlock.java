package com.mrlocalhost.artisanalblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtisanalBlock extends BaseEntityBlock {

    public static final IntegerProperty GLOW = IntegerProperty.create("glow", 0, 15);
    public static final MapCodec<ArtisanalBlock> CODEC = simpleCodec(ArtisanalBlock::new);

    public ArtisanalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(GLOW, 0));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOW);
    }

    /* ************ */
    /* BLOCK ENTITY */
    /* ************ */
    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ArtisanalBlockEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state,
            @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
            @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {

            //light level modification
            if (player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.GLOWSTONE_DUST) && !level.isClientSide()) {
                level.setBlockAndUpdate(pos, artisanalBlockEntity.getBlockState().setValue(GLOW,Integer.min(artisanalBlockEntity.getBlockState().getValue(GLOW)+1,15)));
                return InteractionResult.SUCCESS;
            } else if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !level.isClientSide() && player.isCrouching()) {
                level.setBlockAndUpdate(pos, artisanalBlockEntity.getBlockState().setValue(GLOW,Integer.max(artisanalBlockEntity.getBlockState().getValue(GLOW)-1,0)));
                return InteractionResult.SUCCESS;
            }

            if (hand.equals(InteractionHand.OFF_HAND) || level.isClientSide() || (!stack.isEmpty() && !(stack.getItem() instanceof BlockItem))) {
                return InteractionResult.SUCCESS;
            }
            Direction side = hitResult.getDirection();
            int slot = side.get3DDataValue();
            if(!stack.isEmpty() && !player.isCrouching()) { //hand has a block
                artisanalBlockEntity.clearSlot(slot); //empty slot before attempting to fill it
                artisanalBlockEntity.inventory.insertItem(slot, stack.copy(), false);
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if (stack.isEmpty() && !player.isCrouching()) { //if hand is populated
                artisanalBlockEntity.clearSlot(slot); //empty item from slot in block
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
            System.err.println();
            for (int i = 0; i <= 5; i++ ) {
                String name = artisanalBlockEntity.inventory.getStackInSlot(i).getItemName().getString();
                System.err.println("Direction ["+ ArtisanalBlocksConstants.BLOCK_FACE_POS.get(i).getName()+"]: "+name);
            }
            return InteractionResult.CONSUME; //prevent the block from being placed
        }
        return InteractionResult.FAIL;

    }

}
