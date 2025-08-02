package com.mrlocalhost.artisanalblocks.component;

import com.mojang.serialization.Codec;
import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    @SuppressWarnings("all")
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(ArtisanalBlocks.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIGHT_STATE = register("light_state",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LIGHT_LEVEL = register("light_level",
            builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PLAYER_STATE = register("player_state",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PASSIVE_LEVEL = register("passive_state",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HOSTILE_LEVEL = register("hostile_state",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> SOURCE_COORDINATES = register("source_coordinates",
            builder -> builder.persistent(BlockPos.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_DOWN = register("face_data_down",
            builder -> builder.persistent(ItemStack.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_UP = register("face_data_up",
            builder -> builder.persistent(ItemStack.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_NORTH = register("face_data_north",
            builder -> builder.persistent(ItemStack.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_SOUTH = register("face_data_south",
            builder -> builder.persistent(ItemStack.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_WEST = register("face_data_west",
            builder -> builder.persistent(ItemStack.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> FACE_DATA_EAST = register("face_data_east",
            builder -> builder.persistent(ItemStack.CODEC));

    private static <T>DeferredHolder<DataComponentType<?>,DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
