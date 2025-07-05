package com.mrlocalhost.artisanalblocks.item;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArtisanalBlocks.MOD_ID);

    public static final Supplier<CreativeModeTab> ARTISANALBLOCKS_TAB = CREATIVE_MODE_TAB.register(ArtisanalBlocks.MOD_ID+"_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ARTIST_PALETTE.get()))
                    .title(Component.translatable("creativetab."+ArtisanalBlocks.MOD_ID+".name"))
                    .displayItems(
                        (parameters, output) -> {
                            output.accept(ModItems.ARTIST_PALETTE);
                            output.accept(ModItems.PAINTBRUSH);
                            output.accept(ModItems.CLEANING_CLOTH);
                            output.accept(ModBlocks.ARTISANAL_BLOCK);
                        })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
