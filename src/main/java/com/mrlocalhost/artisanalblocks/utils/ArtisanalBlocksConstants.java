package com.mrlocalhost.artisanalblocks.utils;

import net.minecraft.core.Direction;
import java.util.Map;

public class ArtisanalBlocksConstants {

    public static final int MAX_PALETTE_DAMAGE = 512;

    public static final Map<Integer, Direction> BLOCK_FACE_POS = Map.of(
        Direction.DOWN.get3DDataValue(), Direction.DOWN,
        Direction.UP.get3DDataValue(), Direction.UP,
        Direction.NORTH.get3DDataValue(), Direction.NORTH,
        Direction.SOUTH.get3DDataValue(), Direction.SOUTH,
        Direction.WEST.get3DDataValue(), Direction.WEST,
        Direction.EAST.get3DDataValue(), Direction.EAST
    );

}
