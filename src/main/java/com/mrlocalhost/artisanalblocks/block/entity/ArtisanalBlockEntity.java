package com.mrlocalhost.artisanalblocks.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtisanalBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(6) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(level instanceof ServerLevel) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public void clearSlot(int slot) {
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
    }

    public ArtisanalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ARTISANAL_BLOCK_BE.get(), pos, blockState);
    }

    //drop nothing and clear item stacks
    public void drops() {
        for (int i = 0; i <= 5; i++ ) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        drops();
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory").get());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
