package com.mrlocalhost.artisanalblocks.screen.custom;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;

public class ArtisanalImageButton extends ImageButton {
    public ArtisanalImageButton(int x, int y, int width, int height, WidgetSprites sprites, OnPress onPress) {
        super(x, y, width, height, sprites, onPress);
    }

    public WidgetSprites getWidgetSprites() {
        return this.sprites;
    }
}
