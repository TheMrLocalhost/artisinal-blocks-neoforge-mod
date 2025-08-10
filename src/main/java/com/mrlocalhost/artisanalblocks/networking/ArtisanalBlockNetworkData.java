package com.mrlocalhost.artisanalblocks.networking;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

public record ArtisanalBlockNetworkData(BlockPos blockPos, int config, int value) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArtisanalBlockNetworkData> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID, "artisanal_block_packet"));

    public static final StreamCodec<ByteBuf, ArtisanalBlockNetworkData> STREAM_CODEC = StreamCodec.composite(
            (StreamCodec<? super ByteBuf, BlockPos>) EntityDataSerializers.BLOCK_POS.codec(),
            ArtisanalBlockNetworkData::blockPos,
            ByteBufCodecs.VAR_INT,
            ArtisanalBlockNetworkData::config,
            ByteBufCodecs.VAR_INT,
            ArtisanalBlockNetworkData::value,
            ArtisanalBlockNetworkData::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
