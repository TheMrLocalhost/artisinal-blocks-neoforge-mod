package com.mrlocalhost.artisanalblocks.item;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mrlocalhost.artisanalblocks.item.custom.CleaningClothItem;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArtisanalBlocks.MOD_ID);

    public static final DeferredItem<Item> ARTIST_PALETTE =
        ITEMS.registerItem("artist_palette", Item::new, new Item.Properties()
            .stacksTo(1)
            .durability(ArtisanalBlocksConstants.MAX_PALETTE_DAMAGE)
            .fireResistant());
    public static final DeferredItem<Item> PAINTBRUSH =
        ITEMS.registerItem("paintbrush", Item::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> CLEANING_CLOTH =
        ITEMS.registerItem("cleaning_cloth", CleaningClothItem::new, new Item.Properties()
            .stacksTo(1));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
