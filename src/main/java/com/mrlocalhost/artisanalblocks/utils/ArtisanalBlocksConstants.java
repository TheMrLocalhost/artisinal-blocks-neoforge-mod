package com.mrlocalhost.artisanalblocks.utils;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ArtisanalBlocksConstants {

    public static final int MAX_PALETTE_DAMAGE = 512;

    public static final List<Direction> BLOCK_FACE_POS = List.of(
        Direction.DOWN,
        Direction.UP,
        Direction.NORTH,
        Direction.SOUTH,
        Direction.WEST,
        Direction.EAST
    );

    public static final Map<ArtisanalBlockConfigs.BLOCK_OPTIONS, BooleanProperty> BLOCK_CONFIG_PROPERTY_MAP = Map.ofEntries(
      entry(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT, ArtisanalBlock.GLOW),
        entry(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE, ArtisanalBlock.PLAYER_PASSIBLE),
        entry(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE, ArtisanalBlock.PASSIVE_PASSIBLE),
        entry(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE, ArtisanalBlock.HOSTILE_PASSIBLE)
    );

    public static final int MAX_LIGHT_LEVEL = 15;
    public static final int MIN_LIGHT_LEVEL = 0;

}
