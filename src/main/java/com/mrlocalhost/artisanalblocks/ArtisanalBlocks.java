package com.mrlocalhost.artisanalblocks;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.entity.ModBlockEntities;
import com.mrlocalhost.artisanalblocks.block.entity.renderer.ArtisanalBlockEntityRenderer;
import com.mrlocalhost.artisanalblocks.component.ModDataComponents;
import com.mrlocalhost.artisanalblocks.item.ModCreativeModeTabs;
import com.mrlocalhost.artisanalblocks.item.ModItems;
import com.mrlocalhost.artisanalblocks.networking.ArtisanalBlockNetworkData;
import com.mrlocalhost.artisanalblocks.networking.handlers.ArtisanalBlockNetworkHandler;
import com.mrlocalhost.artisanalblocks.screen.ModMenuTypes;
import com.mrlocalhost.artisanalblocks.screen.custom.ArtisanalBlockScreen;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ArtisanalBlocks.MOD_ID)
public class ArtisanalBlocks {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "artisanalblocks";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // NFML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ArtisanalBlocks(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {


    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
    public static class ServerModEvents {

        @SubscribeEvent // on the mod event bus
        public static void register(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);
            registrar.playBidirectional(
                    ArtisanalBlockNetworkData.TYPE,
                    ArtisanalBlockNetworkData.STREAM_CODEC,
                    new DirectionalPayloadHandler<>(
                            ArtisanalBlockNetworkHandler::handleDataOnClient,
                            ArtisanalBlockNetworkHandler::handleDataOnMain
                    )
            );
        }

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent // on the mod event bus
        public static void register(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);
            registrar.playBidirectional(
                    ArtisanalBlockNetworkData.TYPE,
                    ArtisanalBlockNetworkData.STREAM_CODEC,
                    new DirectionalPayloadHandler<>(
                            ArtisanalBlockNetworkHandler::handleDataOnClient,
                            ArtisanalBlockNetworkHandler::handleDataOnMain
                    )
            );
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.ARTISANAL_BLOCK_BE.get(), ArtisanalBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.ARTISANAL_BLOCK_MENU.get(), ArtisanalBlockScreen::new);
        }
    }
}
