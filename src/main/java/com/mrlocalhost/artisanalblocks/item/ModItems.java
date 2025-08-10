package com.mrlocalhost.artisanalblocks.item;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.item.custom.*;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

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

    public static final DeferredItem<Item> PLAYER_DUST =
            ITEMS.registerItem("player_dust", Item::new, new Item.Properties()
                    .stacksTo(64));
    public static final DeferredItem<Item> PASSIVE_DUST =
            ITEMS.registerItem("passive_dust", Item::new, new Item.Properties()
                    .stacksTo(64));
    public static final DeferredItem<Item> HOSTILE_DUST =
            ITEMS.registerItem("hostile_dust", Item::new, new Item.Properties()
                    .stacksTo(64));

    public static final DeferredItem<Item> PLAYER_ROD =
        ITEMS.registerItem("player_rod", PlayerRodItem::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> PASSIVE_ROD =
        ITEMS.registerItem("passive_rod", PassiveRodItem::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> HOSTILE_ROD =
        ITEMS.registerItem("hostile_rod", HostileRodItem::new, new Item.Properties()
            .stacksTo(1));
    public static final DeferredItem<Item> LIGHTING_ROD =
        ITEMS.registerItem("lighting_rod", LightingRodItem::new, new Item.Properties()
            .stacksTo(1));

    public static final DeferredItem<Item> ARTISANAL_CHISEL =
        ITEMS.registerItem("artisanal_chisel", ArtisanalChiselItem::new, new Item.Properties()
            .stacksTo(1));

    public static final DeferredItem<Item> EYEDROPPER =
        ITEMS.registerItem("eyedropper", EyedropperItem::new, new Item.Properties()
            .stacksTo(1));

    public static final DeferredItem<Item> GLOWSTONE_GEM =
        ITEMS.registerItem("glowstone_gem", Item::new, new Item.Properties()
                .stacksTo(64));
    public static final DeferredItem<Item> PLAYER_GEM =
        ITEMS.registerItem("player_gem", Item::new, new Item.Properties()
                .stacksTo(64));
    public static final DeferredItem<Item> PASSIVE_GEM =
        ITEMS.registerItem("passive_gem", Item::new, new Item.Properties()
                .stacksTo(64));
    public static final DeferredItem<Item> HOSTILE_GEM =
        ITEMS.registerItem("hostile_gem", Item::new, new Item.Properties()
                .stacksTo(64));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
