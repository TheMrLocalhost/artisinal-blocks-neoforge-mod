package com.mrlocalhost.artisanalblocks.component;

import com.mojang.serialization.Codec;
import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ArtisanalBlocks.MOD_ID);

    //Block type any
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Block>> BASE_BLOCK = register("base_block",
            builder -> builder.persistent(Block.CODEC.codec()));
    //Integer type from 0 to 15
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GLOW_LEVEL = register("glow_level",
            builder -> builder.persistent(ExtraCodecs.intRange(0, 15)).networkSynchronized(ByteBufCodecs.VAR_INT));//Integer type from 0 to 15
    //RGB (no alpha channel)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RGB_TINT = register("rgb_tint",
            builder -> builder.persistent(ExtraCodecs.RGB_COLOR_CODEC));
    //Passible by players
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PASS_PLAYERS = register("pass_players",
            builder -> builder.persistent(Codec.BOOL));
    //Passible by passive mobs
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PASS_PASSIVES = register("pass_passives",
            builder -> builder.persistent(Codec.BOOL));
    //Passible by hostile mobs
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PASS_HOSTILES = register("pass_hostiles",
            builder -> builder.persistent(Codec.BOOL));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
