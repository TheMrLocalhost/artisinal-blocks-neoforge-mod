package com.mrlocalhost.artisanalblocks.item;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArtisanalBlocks.MOD_ID);

    public static final DeferredItem<Item> ARTIST_PALETTE = registerItem("artist_palette", new Item.Properties()
            .stacksTo(1)
            .durability(ArtisanalBlocksConstants.MAX_PALETTE_DAMAGE)
            .fireResistant());
    public static final DeferredItem<Item> PAINTBRUSH = registerItem("paintbrush", new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> CLEANING_CLOTH = registerItem("cleaning_cloth", new Item.Properties()
            .stacksTo(1));

    private static DeferredItem<Item> registerItem(String name, Item.Properties itemProperties) {
        return ITEMS.registerItem(name, Item::new, itemProperties);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
