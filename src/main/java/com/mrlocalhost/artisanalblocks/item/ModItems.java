package com.mrlocalhost.artisanalblocks.item;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mrlocalhost.artisanalblocks.item.custom.CleaningClothItem;
import com.mrlocalhost.artisanalblocks.item.custom.PlayerPassageDust;
import com.mrlocalhost.artisanalblocks.item.custom.PassivePassageDust;
import com.mrlocalhost.artisanalblocks.item.custom.HostilePassageDust;

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
    public static final DeferredItem<Item> PLAYER_PASSAGE_DUST =
        ITEMS.registerItem("player_passage_dust", PlayerPassageDust::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> PASSIVE_PASSAGE_DUST =
        ITEMS.registerItem("passive_passage_dust", PassivePassageDust::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> HOSTILE_PASSAGE_DUST =
        ITEMS.registerItem("hostile_passage_dust", HostilePassageDust::new, new Item.Properties()
            .stacksTo(1));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
