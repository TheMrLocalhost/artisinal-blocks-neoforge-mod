package com.mrlocalhost.artisanalblocks.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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

    public static final List<Vec3> BLOCK_CORNERS = List.of(
        new Vec3(0, 0, 0),
        new Vec3(0, 0, 1),
        new Vec3(0, 1, 0),
        new Vec3(0, 1, 1),
        new Vec3(1, 0, 0),
        new Vec3(1, 0, 1),
        new Vec3(1, 1, 0),
        new Vec3(1, 1, 1)
    );

}
