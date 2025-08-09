package com.mrlocalhost.artisanalblocks.screen.custom;

import com.mojang.math.Axis;
import com.mrlocalhost.artisanalblocks.ArtisanalBlocks;
import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.networking.ArtisanalBlockNetworkData;
import com.mrlocalhost.artisanalblocks.networking.handlers.ArtisanalBlockNetworkHandler;
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
    private final ResourceLocation LIGHT_UP_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/up_arrow");
    private final ResourceLocation LIGHT_DOWN_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/down_arrow");
    private final ResourceLocation IGNORED_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/ignored_hover");
    private final ResourceLocation LOW_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/low_hover");
    private final ResourceLocation HIGH_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/high_hover");
    private final ResourceLocation LIGHT_UP_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/up_arrow_hover");
    private final ResourceLocation LIGHT_DOWN_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/down_arrow_hover");
    private final ResourceLocation BLANK_BUTTON = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/blank_button");
    private final ResourceLocation GLOWSTONE_ITEM = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/glowstone_dust");
    private final ResourceLocation PLAYER_DUST_ITEM = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/player_passage_dust");
    private final ResourceLocation PASSIVE_DUST_ITEM = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/passive_passage_dust");
    private final ResourceLocation HOSTILE_DUST_ITEM = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"widget/hostile_passage_dust");

    private final ResourceLocation YELLOW_BOX = ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID,"textures/gui/sprites/yellow_box.png");

    private final WidgetSprites IGNORED_SPRITE = new WidgetSprites(IGNORED_LOCATION, IGNORED_HOVER_LOCATION);
    private final WidgetSprites LOW_SPRITE = new WidgetSprites(LOW_LOCATION, LOW_HOVER_LOCATION);
    private final WidgetSprites HIGH_SPRITE = new WidgetSprites(HIGH_LOCATION, HIGH_HOVER_LOCATION);
    private final WidgetSprites LIGHT_UP_BUTTON_WIDGET = new WidgetSprites(LIGHT_UP_LOCATION, LIGHT_UP_HOVER_LOCATION);
    private final WidgetSprites LIGHT_DOWN_BUTTON_WIDGET = new WidgetSprites(LIGHT_DOWN_LOCATION, LIGHT_DOWN_HOVER_LOCATION);
    private final WidgetSprites BLANK_BUTTON_WIDGET = new WidgetSprites(BLANK_BUTTON, BLANK_BUTTON);
    private final WidgetSprites GLOWSTONE_WIDGET = new WidgetSprites(GLOWSTONE_ITEM, GLOWSTONE_ITEM);
    private final WidgetSprites PLAYER_DUST_WIDGET = new WidgetSprites(PLAYER_DUST_ITEM, PLAYER_DUST_ITEM);
    private final WidgetSprites PASSIVE_DUST_WIDGET = new WidgetSprites(PASSIVE_DUST_ITEM, PASSIVE_DUST_ITEM);
    private final WidgetSprites HOSTILE_DUST_WIDGET = new WidgetSprites(HOSTILE_DUST_ITEM, HOSTILE_DUST_ITEM);

    private final Map<String, ArtisanalImageButton> CUSTOM_BUTTONS = new HashMap<>();
    private int lightLevelValue;
    private final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArtisanalBlocks.MOD_ID, "textures/gui/artisanal_block/artisanal_block_gui.png");

    private final List<WidgetSprites> INITIAL_SCREEN_STATES = new ArrayList<>();

    private final int MAX_LIGHT_LEVEL = 15;
    private final int MIN_LIGHT_LEVEL = 0;

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
        lightLevelValue = menu.blockEntity.getBlockState().getValue(ArtisanalBlock.GLOW_VALUE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int cX = ((width - imageWidth) / 2) + imageWidth/2;
        int cY = ((height - imageHeight) / 2) + imageHeight/2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        int lightLevelY = cY-67;
        int col0 = cX-8;
        int col3 = cX+32;
        int row1 = cY-49;
        int row2 = cY-31;

        this.addRenderableOnly(new ArtisanalImageButton(col3, lightLevelY, 16, 16, BLANK_BUTTON_WIDGET, null));
        this.addRenderableOnly(new ArtisanalImageButton(col0, row1, 16, 16, GLOWSTONE_WIDGET, null))
                .setTooltip(Tooltip.create(Component.literal("Light Redstone Config")));
        this.addRenderableOnly(new ArtisanalImageButton(col0, row2, 16, 16, PLAYER_DUST_WIDGET, null))
                .setTooltip(Tooltip.create(Component.literal("Player Passage Config")));
        this.addRenderableOnly(new ArtisanalImageButton(col3, row1, 16, 16, PASSIVE_DUST_WIDGET, null))
                .setTooltip(Tooltip.create(Component.literal("Passive Passage Config")));
        this.addRenderableOnly(new ArtisanalImageButton(col3, row2, 16, 16, HOSTILE_DUST_WIDGET, null))
                .setTooltip(Tooltip.create(Component.literal("Hostile Redstone Config")));
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

        int cX = ((width - imageWidth) / 2) + imageWidth/2;
        int cY = ((height - imageHeight) / 2) + imageHeight/2;

        String lightLevelString = (this.lightLevelValue<10) ? " "+this.lightLevelValue : String.valueOf(this.lightLevelValue);
        int lightLevelY = cY-67;
        int col3 = cX+32;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().rotateAround(Axis.ZN.rotationDegrees(180.0F), col3, lightLevelY, 0);
        guiGraphics.pose().translate(-16, -16, 0);
        guiGraphics.blit(YELLOW_BOX, col3, lightLevelY, 0, 0, 16, this.lightLevelValue+1, 16, 16);
        guiGraphics.pose().popPose();

        guiGraphics.drawString(this.font, Component.literal(lightLevelString), cX+34, lightLevelY+4, 0, false);
        guiGraphics.drawString(this.font, "D", cX-62, cY-62, 4144959, false);
        guiGraphics.drawString(this.font, "N", cX-62, cY-45, 4144959, false);
        guiGraphics.drawString(this.font, "W", cX-62, cY-27, 4144959, false);
        guiGraphics.drawString(this.font, "U", cX-36, cY-62, 4144959, false);
        guiGraphics.drawString(this.font, "S", cX-36, cY-45, 4144959, false);
        guiGraphics.drawString(this.font, "E", cX-36, cY-27, 4144959, false);
        guiGraphics.drawString(this.font, "Light Level", cX+14, lightLevelY-10, 4144959, false);
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
        lightLevelValue = menu.blockEntity.getBlockState().getValue(ArtisanalBlock.GLOW_VALUE);
    }

    private void initButtons() {

        int cX = ((width - imageWidth) / 2) + imageWidth/2;
        int cY = ((height - imageHeight) / 2) + imageHeight/2;
        int lightLevelY = cY-67;
        int col1 = cX+12;
        int col3 = cX+52;
        int row1 = cY-49;
        int row2 = cY-31;

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT.name(),
            new ArtisanalImageButton(col1, row1, 16, 16, INITIAL_SCREEN_STATES.get(0),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE.name(),
            new ArtisanalImageButton(col1, row2, 16, 16, INITIAL_SCREEN_STATES.get(1),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE.name(),
            new ArtisanalImageButton(col3, row1, 16, 16, INITIAL_SCREEN_STATES.get(2),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE); }));

        CUSTOM_BUTTONS.put(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE.name(),
            new ArtisanalImageButton(col3, row2, 16, 16, INITIAL_SCREEN_STATES.get(3),
        button -> { selectOption(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE); }));

        CUSTOM_BUTTONS.put("LIGHT_UP",
            new ArtisanalImageButton(col3, lightLevelY, 16, 16, LIGHT_UP_BUTTON_WIDGET,
        button -> { lightLevelButtonPressed(true);  }));
        CUSTOM_BUTTONS.put("LIGHT_DOWN",
            new ArtisanalImageButton(col1, lightLevelY, 16, 16, LIGHT_DOWN_BUTTON_WIDGET,
        button -> { lightLevelButtonPressed(false);  }));
    }

    private void addButtons() {
        this.CUSTOM_BUTTONS.forEach( (name, button) -> {
            addRenderWidget(button);
        });
    }

    private void swapButton(String name, ArtisanalImageButton originalButton, ArtisanalImageButton newButton) {
        CUSTOM_BUTTONS.replace(name, newButton);
        this.removeWidget(originalButton);
        this.addRenderWidget(newButton);
    }

    private void addRenderWidget(ArtisanalImageButton button) {
        String hoverText = "";
        if (button.getWidgetSprites() == IGNORED_SPRITE) {
            hoverText = "Ignored";
        } else if (button.getWidgetSprites() == LOW_SPRITE) {
            hoverText = "Active Low";
        } else if (button.getWidgetSprites() == HIGH_SPRITE) {
            hoverText = "Active High";
        } else if (button.getWidgetSprites() == LIGHT_UP_BUTTON_WIDGET) {
            hoverText = "Increase";
        } else if (button.getWidgetSprites() == LIGHT_DOWN_BUTTON_WIDGET) {
            hoverText = "Decrease";
        }
        this.addRenderableWidget(button).setTooltip(Tooltip.create(Component.literal(hoverText)));
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

    private void lightLevelButtonPressed(boolean isUp) {
        int oldLevel = lightLevelValue;
        if (isUp) {
            lightLevelValue = (lightLevelValue == MAX_LIGHT_LEVEL) ? lightLevelValue : lightLevelValue +1;
        } else {
            lightLevelValue = (lightLevelValue == MIN_LIGHT_LEVEL) ? lightLevelValue : lightLevelValue -1;
        }
        if (oldLevel != lightLevelValue) {
            PacketDistributor.sendToServer(new ArtisanalBlockNetworkData(menu.blockEntity.getBlockPos(), ArtisanalBlockNetworkHandler.LIGHT_LEVEL_CONFIG_FLAG, lightLevelValue));
        }
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
        swapButton(config.name(), originalButton, newButton);
        //this.clearWidgets();
        //this.addButtons();
    }
}
