package com.mrlocalhost.artisanalblocks.block.entity;

import com.mrlocalhost.artisanalblocks.item.ModItems;
import com.mrlocalhost.artisanalblocks.screen.custom.ArtisanalBlockMenu;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtisanalBlockEntity extends BlockEntity implements MenuProvider {

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

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int id) {
            ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(id);
            return ArtisanalBlockEntity.this.blockConfig.getRedstoneOption(config).getStateInt();
        }
        @Override
        public void set(int id, int value) {
            ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(id);
            ArtisanalBlockEntity.this.blockConfig.setRedstoneOption(config, ArtisanalBlockConfigs.REDSTONE_OPTIONS.getState(value));
        }
        @Override
        public int getCount() { return ArtisanalBlockConfigs.TOTAL_REDSTONE_OPTIONS; }
    };

    private final ArtisanalBlockConfigs blockConfig = new ArtisanalBlockConfigs();

    public void setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS option, ArtisanalBlockConfigs.REDSTONE_OPTIONS value) {
        this.blockConfig.setRedstoneOption(option, value);
    }

    public void clearSlot(int slot) {
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
    }

    public ArtisanalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ARTISANAL_BLOCK_BE.get(), pos, blockState);
    }

    public ArtisanalBlockConfigs.REDSTONE_OPTIONS getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS config) {
        return this.blockConfig.getRedstoneOption(config);
    }

    //drop nothing and clear item stacks
    public void drops() {
        for (int i = 0; i <= 5; i++ ) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("block_config", blockConfig.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("block_config")) {
            blockConfig.deserializeNBT(registries, tag.getCompound("block_config"));
        }
    }

    @Override
    public boolean hasCustomOutlineRendering(@NotNull Player player) {
        return player.getMainHandItem().is(ModItems.ARTISANAL_CHISEL);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.artisanalblocks.artisanal_block.display_name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ArtisanalBlockMenu(containerId, playerInventory, this);
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
