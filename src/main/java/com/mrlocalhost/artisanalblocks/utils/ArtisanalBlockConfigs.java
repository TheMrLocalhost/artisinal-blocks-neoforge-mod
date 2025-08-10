package com.mrlocalhost.artisanalblocks.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ArtisanalBlockConfigs implements INBTSerializable<CompoundTag> {

    private final EnumMap<BLOCK_OPTIONS, REDSTONE_OPTIONS> redstoneStates = new EnumMap<>(BLOCK_OPTIONS.class);

    public static final int TOTAL_REDSTONE_OPTIONS = 4;

    public ArtisanalBlockConfigs() {
        this.redstoneStates.put(BLOCK_OPTIONS.LIGHT, REDSTONE_OPTIONS.IGNORED);
        this.redstoneStates.put(BLOCK_OPTIONS.PLAYER_PASSAGE, REDSTONE_OPTIONS.IGNORED);
        this.redstoneStates.put(BLOCK_OPTIONS.PASSIVE_PASSAGE, REDSTONE_OPTIONS.IGNORED);
        this.redstoneStates.put(BLOCK_OPTIONS.HOSTILE_PASSAGE, REDSTONE_OPTIONS.IGNORED);
    }

    public REDSTONE_OPTIONS getRedstoneOption(BLOCK_OPTIONS config) {
        return redstoneStates.get(config);
    }

    public void setRedstoneOption(BLOCK_OPTIONS config, REDSTONE_OPTIONS value) {
        redstoneStates.replace(config, value);
    }

    public enum BLOCK_OPTIONS {
        LIGHT(0),
        PLAYER_PASSAGE(1),
        PASSIVE_PASSAGE(2),
        HOSTILE_PASSAGE(3);

        private final int state;

        BLOCK_OPTIONS(int i) {
            this.state = i;
        }

        public static BLOCK_OPTIONS getState(int i) {
            return switch (i) {
                case (0) -> LIGHT;
                case (1) -> PLAYER_PASSAGE;
                case (2) -> PASSIVE_PASSAGE;
                case (3) -> HOSTILE_PASSAGE;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }

        public int getStateInt() {
            return this.state;
        }

    }

    public enum REDSTONE_OPTIONS {

        IGNORED(0),
        LOW(1),
        HIGH(2);

        private final int state;

        REDSTONE_OPTIONS(int i) {
            this.state = i;
        }

        public static REDSTONE_OPTIONS getState(int i) {
            return switch (i) {
                case (0) -> IGNORED;
                case (1) -> LOW;
                case (2) -> HIGH;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }

        public int getStateInt() {
            return this.state;
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        for (BLOCK_OPTIONS option: redstoneStates.keySet()) {
            nbt.putInt(option.name(), redstoneStates.get(option).getStateInt());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag compoundTag) {

        for(BLOCK_OPTIONS option: redstoneStates.keySet()) {
            if (compoundTag.contains(option.name())) {
                REDSTONE_OPTIONS redstoneConfig = REDSTONE_OPTIONS.getState(compoundTag.getInt(option.name()));
                redstoneStates.replace(option, redstoneConfig);
            }
        }
    }
}
