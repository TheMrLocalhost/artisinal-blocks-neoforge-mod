package com.mrlocalhost.artisanalblocks.screen.custom;

import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.networking.ArtisanalBlockNetworkData;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtisanalBlockScreen extends AbstractContainerScreen<ArtisanalBlockMenu> {

    private final ResourceLocation IGNORED_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/ignored");
    private final ResourceLocation LOW_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/low");
    private final ResourceLocation HIGH_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/high");
    private final ResourceLocation IGNORED_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/ignored_hover");
    private final ResourceLocation LOW_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/low_hover");
    private final ResourceLocation HIGH_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/high_hover");

    private final WidgetSprites IGNORED_SPRITE = new WidgetSprites(IGNORED_LOCATION, IGNORED_HOVER_LOCATION);
    private final WidgetSprites LOW_SPRITE = new WidgetSprites(LOW_LOCATION, LOW_HOVER_LOCATION);
    private final WidgetSprites HIGH_SPRITE = new WidgetSprites(HIGH_LOCATION, HIGH_HOVER_LOCATION);

    private final Map<String, ArtisanalImageButton> CUSTOM_BUTTONS = new HashMap<>();
    private final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID, "textures/gui/artisanal_block/artisanal_block_gui.png");

    private final List<WidgetSprites> INITIAL_SCREEN_STATES = new ArrayList<>();

    public ArtisanalBlockScreen(ArtisanalBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        for (int i = 0; i < ArtisanalBlockConfigs.TOTAL_REDSTONE_OPTIONS; i++) {
            int state = menu.blockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(i)).getStateInt();
            switch(state) {
                case 2: {
                    INITIAL_SCREEN_STATES.add(HIGH_SPRITE); break;
                } case 1: {
                    INITIAL_SCREEN_STATES.add(LOW_SPRITE); break;
                } case 0: default: {
                    INITIAL_SCREEN_STATES.add(IGNORED_SPRITE); break;
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void init() {
        super.init();
        this.initButtons();
        this.addButtons();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        syncScreenButtons();
    }

    private void syncScreenButtons() {
        for (int i = 0; i < ArtisanalBlockConfigs.TOTAL_REDSTONE_OPTIONS; i++) {
            ArtisanalBlockConfigs.BLOCK_OPTIONS config = ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(i);
            ArtisanalBlockConfigs.REDSTONE_OPTIONS newValue = menu.blockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.getState(i));
            ArtisanalImageButton currentButton = CUSTOM_BUTTONS.get(config.name());

            switch(newValue) {
                case ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED: {
                    if (currentButton.getWidgetSprites() != IGNORED_SPRITE) {
                        updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), IGNORED_SPRITE, config);
                    }
                    break;
                }
                case ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW: {
                    if (currentButton.getWidgetSprites() != LOW_SPRITE) {
                        updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), LOW_SPRITE, config);
                    }
                    break;
                }
                case ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH: default: {
                    if (currentButton.getWidgetSprites() != HIGH_SPRITE) {
                        updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), HIGH_SPRITE, config);
                    }
                    break;
                }
            }
        }
    }

    private void initButtons() {

        int centerX = ((width - imageWidth) / 2) + imageWidth/2;
        int centerY = ((height - imageHeight) / 2) + imageHeight/2;

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT.name(),
            new ArtisanalImageButton(centerX+12, centerY-76, 16, 16, INITIAL_SCREEN_STATES.get(0),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE.name(),
            new ArtisanalImageButton(centerX+12, centerY-58, 16, 16, INITIAL_SCREEN_STATES.get(1),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE.name(),
            new ArtisanalImageButton(centerX+12, centerY-40, 16, 16, INITIAL_SCREEN_STATES.get(2),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE.name(),
            new ArtisanalImageButton(centerX+12, centerY-22, 16, 16, INITIAL_SCREEN_STATES.get(3),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE); }));
    }

    private void addButtons() {
        this.CUSTOM_BUTTONS.forEach( (name, button) -> {
            this.addRenderableWidget(button).setTooltip(Tooltip.create(Component.literal(name)));
        });
    }

    private void selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS config) {
        ArtisanalBlockConfigs.REDSTONE_OPTIONS prevState = menu.blockEntity.getBlockConfig(config);
        ArtisanalBlockConfigs.REDSTONE_OPTIONS newState;
        switch(prevState) {
            case ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED: {
                newState = ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW;
                updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), LOW_SPRITE, config);
                break;
            }
            case ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW: {
                newState = ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH;
                updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), HIGH_SPRITE, config);
                break;
            }
            case ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH: default: {
                newState = ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED;
                updateButtonSprite(CUSTOM_BUTTONS.get(config.name()), IGNORED_SPRITE, config);
                break;
            }
        }

        PacketDistributor.sendToServer(new ArtisanalBlockNetworkData(menu.blockEntity.getBlockPos(), config.getStateInt(), newState.getStateInt()));
    }

    private void updateButtonSprite(ArtisanalImageButton originalButton, WidgetSprites newSprite, ArtisanalBlockConfigs.BLOCK_OPTIONS config) {
        ArtisanalImageButton newButton = new ArtisanalImageButton(
                originalButton.getX(), originalButton.getY(),
                originalButton.getWidth(), originalButton.getHeight(),
                newSprite,
                button -> {
                    selectOption(config);
                }
        );
        CUSTOM_BUTTONS.replace(config.name(), newButton);
        this.clearWidgets();
        this.addButtons();
    }
}
