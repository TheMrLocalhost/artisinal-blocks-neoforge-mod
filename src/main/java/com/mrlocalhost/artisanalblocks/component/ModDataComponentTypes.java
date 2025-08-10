package com.mrlocalhost.artisanalblocks.component;

import com.mojang.serialization.Codec;
import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    @SuppressWarnings("all")
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(ArtisanalBlocks.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIGHT_STATE = register("light",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LIGHT_LEVEL = register("light_level",
            builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PLAYER_STATE = register("player",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PASSIVE_STATE = register("passive",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HOSTILE_STATE = register("hostile",
            builder -> builder.persistent(Codec.BOOL));

    public static final List<DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>>> REDSTONE_COMPONENT_STATES =
            List.of(ModDataComponentTypes.LIGHT_STATE, ModDataComponentTypes.PLAYER_STATE, ModDataComponentTypes.PASSIVE_STATE, ModDataComponentTypes.HOSTILE_STATE);

    private static <T>DeferredHolder<DataComponentType<?>,DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
