package com.mrlocalhost.artisanalblocks.networking.handlers;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.networking.ArtisanalBlockNetworkData;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ArtisanalBlockNetworkHandler {

    public static void handleDataOnMain(final ArtisanalBlockNetworkData data, final IPayloadContext context) {
        BlockPos blockPos = data.blockPos();
        ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(data.config());
        ArtisanalBlockConfigs.REDSTONE_OPTIONS option = ArtisanalBlockConfigs.REDSTONE_OPTIONS.getState(data.value());
        ServerLevel level = ((ServerLevel) context.player().level());
        BlockState blockState = level.getBlockState(blockPos);
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            artisanalBlockEntity.setBlockConfig(config, option);
            artisanalBlockEntity.setChanged();
            ((ArtisanalBlock) level.getBlockState(blockPos).getBlock()).updateRedstoneState(blockState, level, blockPos, config);
        }
    }

}
