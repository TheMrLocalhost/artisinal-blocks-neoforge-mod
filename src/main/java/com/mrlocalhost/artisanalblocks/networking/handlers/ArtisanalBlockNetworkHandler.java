package com.mrlocalhost.artisanalblocks.networking.handlers;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.networking.ArtisanalBlockNetworkData;
import com.mrlocalhost.artisanalblocks.screen.custom.ArtisanalBlockMenu;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ArtisanalBlockNetworkHandler {

    public static final int LIGHT_LEVEL_CONFIG_FLAG = 4;

    public static void handleDataOnClient(final ArtisanalBlockNetworkData data, final IPayloadContext context) {
        BlockPos blockPos = data.blockPos();
        Level level = context.player().level();
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity) {
            if (context.player().containerMenu instanceof ArtisanalBlockMenu menu) {
                if (data.config() < ArtisanalBlockConfigs.TOTAL_REDSTONE_OPTIONS) {
                    ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(data.config());
                    ArtisanalBlockConfigs.REDSTONE_OPTIONS option = ArtisanalBlockConfigs.REDSTONE_OPTIONS.getState(data.value());
                    menu.setArtisanalData(config, option);
                } else { //non-BLOCK_OPTIONS values
                    switch(data.config()) {
                        case LIGHT_LEVEL_CONFIG_FLAG: {
                            //do nothing
                            break;
                        }
                        default: {}
                    }
                }
            }
        }
    }

    public static void handleDataOnMain(final ArtisanalBlockNetworkData data, final IPayloadContext context) {
        BlockPos blockPos = data.blockPos();
        ServerLevel level = ((ServerLevel) context.player().level());
        ServerPlayer player = ((ServerPlayer) context.player());
        BlockState blockState = level.getBlockState(blockPos);
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            if (data.config() < ArtisanalBlockConfigs.TOTAL_REDSTONE_OPTIONS) {
                ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(data.config());
                ArtisanalBlockConfigs.REDSTONE_OPTIONS option = ArtisanalBlockConfigs.REDSTONE_OPTIONS.getState(data.value());
                artisanalBlockEntity.setBlockConfig(config, option);
                artisanalBlockEntity.setChanged();
                ((ArtisanalBlock) level.getBlockState(blockPos).getBlock()).updateRedstoneState(blockState, level, blockPos, config);
                PacketDistributor.sendToPlayersNear(level, player, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 16,
                        new ArtisanalBlockNetworkData(blockPos, config.getStateInt(), option.getStateInt()));
            } else { //non-BLOCK_OPTIONS values
                switch(data.config()) {
                    case LIGHT_LEVEL_CONFIG_FLAG: {
                        int newLightLevel = data.value();
                        level.setBlockAndUpdate(blockPos, blockState.setValue(ArtisanalBlock.GLOW_VALUE, newLightLevel));
                        break;
                    }
                    default: {}
                }
            }
        }
    }
}
