package com.mrlocalhost.artisanalblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtisanalBlock extends BaseEntityBlock {

    public static final IntegerProperty GLOW = IntegerProperty.create("glow", 0, 15);
    public static final BooleanProperty PLAYER_PASSIBLE = BooleanProperty.create("player_passible");
    public static final BooleanProperty HOSTILE_PASSIBLE = BooleanProperty.create("hostile_passible");
    public static final BooleanProperty PASSIVE_PASSIBLE = BooleanProperty.create("passive_passible");

    public static final MapCodec<ArtisanalBlock> CODEC = simpleCodec(ArtisanalBlock::new);

    public ArtisanalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
            .setValue(GLOW, 0)
            .setValue(PLAYER_PASSIBLE, false)
            .setValue(HOSTILE_PASSIBLE, false)
            .setValue(PASSIVE_PASSIBLE, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOW);
        builder.add(PLAYER_PASSIBLE);
        builder.add(HOSTILE_PASSIBLE);
        builder.add(PASSIVE_PASSIBLE);
    }

    /* ************ */
    /* BLOCK ENTITY */
    /* ************ */
    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
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

            if (hand.equals(InteractionHand.OFF_HAND) || level.isClientSide() || (!stack.isEmpty() && (!ArtisanalBlocksUtilities.isPlacableInArtisanalBlock(stack)) && !stack.getItem().equals(ModBlocks.ARTISANAL_BLOCK.asItem()))) {
                if (level.isClientSide()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
            Direction side = hitResult.getDirection();
            int slot = side.get3DDataValue();

            //This conditional block could definitely be simplified, but I cannot be bothered
            if(!stack.isEmpty() && !player.isCrouching()) { //hand has a block
                if (!stack.getItem().equals(ModBlocks.ARTISANAL_BLOCK.asItem())) {
                    artisanalBlockEntity.clearSlot(slot); //empty slot before attempting to fill it
                    ItemStack itemCopy = stack.copy();
                    itemCopy.setCount(1);
                    artisanalBlockEntity.inventory.insertItem(slot, itemCopy, false);
                    player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25F, 2.0F);
                } else if (!artisanalBlockEntity.inventory.getStackInSlot(slot).isEmpty()) {
                    artisanalBlockEntity.clearSlot(slot); //empty slot before attempting to fill it
                    player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25F, 1.0F);
                }
            } else if (stack.isEmpty() && !player.isCrouching()) { //if hand is populated
                if (!artisanalBlockEntity.inventory.getStackInSlot(slot).isEmpty()) {
                    artisanalBlockEntity.clearSlot(slot); //empty item from slot in block
                    player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25F, 1.0F);
                }
            }
            return InteractionResult.CONSUME; //prevent the block from being placed
        }
        return InteractionResult.FAIL;

    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, CollisionContext context) {
        if (!context.isPlacement() && context instanceof EntityCollisionContext entitycollisioncontext) {
            Entity entity = entitycollisioncontext.getEntity();
            if (entity != null) {
                boolean isPlayer = entity instanceof Player;
                boolean isMonster = entity.getType().getCategory().equals(MobCategory.MONSTER);
                boolean isPassive = List.of(
                    MobCategory.CREATURE,
                    MobCategory.AXOLOTLS
                ).contains(entity.getType().getCategory());

                if (entity instanceof FallingBlockEntity
                    || (isPlayer && blockState.getValue(PLAYER_PASSIBLE))
                    || (isMonster && blockState.getValue(HOSTILE_PASSIBLE))
                    || (isPassive && blockState.getValue(PASSIVE_PASSIBLE))
                ) {
                    return Shapes.empty();
                }
            }
        }
        return super.getCollisionShape(blockState, blockGetter, blockPos, context);

    }

}
