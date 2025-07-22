package com.mrlocalhost.artisanalblocks.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArtisanalBlockEntityRenderer implements BlockEntityRenderer<ArtisanalBlockEntity> {

    @SuppressWarnings("unused")
    public ArtisanalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull ArtisanalBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        List<Direction> blockFaces = List.of(
                Direction.DOWN,
                Direction.UP,
                Direction.NORTH,
                Direction.SOUTH,
                Direction.WEST,
                Direction.EAST
        );



        blockFaces.forEach( direction -> {
            ItemStack stack = blockEntity.inventory.getStackInSlot(direction.get3DDataValue());
            if (stack.isEmpty()) {
                stack = ModBlocks.ARTISANAL_BLOCK.toStack();
            }

            if (blockEntity.getLevel() != null) {
                BlockPos sidePos = getRelativePosition(blockEntity.getBlockPos(), direction);
                if (!(blockEntity.getLevel().getBlockState(sidePos).getBlock() instanceof ArtisanalBlock)) {
                    poseStack.pushPose();
                    Vec3 faceTranslations = getFaceTranslation(direction);
                    poseStack.translate(faceTranslations.x, faceTranslations.y, faceTranslations.z); //set translation based on face being rendered
                    Vec3 scales = getFaceScale(direction);
                    poseStack.scale((float) scales.x, (float) scales.y, (float) scales.z);

                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                            getLightLevel(blockEntity.getLevel(), sidePos),
                            OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
                    poseStack.popPose();
                }
            }
        });


    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private BlockPos getRelativePosition(BlockPos pos, Direction direction) {
        return switch (direction) {
            case DOWN -> pos.below();
            case UP -> pos.above();
            case NORTH -> pos.north();
            case SOUTH -> pos.south();
            case WEST -> pos.west();
            case EAST -> pos.east();
        };
    }

    private Vec3 getFaceTranslation(Direction direction) {
        return switch (direction) {
            case DOWN -> new Vec3(0.5, 0.0, 0.5);
            case UP -> new Vec3(0.5, 1.0, 0.5);
            case NORTH -> new Vec3(0.5, 0.5, 0);
            case SOUTH -> new Vec3(0.5, 0.5, 1.0);
            case WEST -> new Vec3(0.0, 0.5, 0.5);
            case EAST -> new Vec3(1.0, 0.5, 0.5);
        };
    }

    private Vec3 getFaceScale(Direction direction) {
        return switch (direction) {
            case DOWN, UP -> new Vec3(1.999, 0.001, 1.999);
            case NORTH, SOUTH -> new Vec3(1.999, 1.999, 0.001);
            case WEST, EAST -> new Vec3(0.001, 1.999, 1.999);
        };
    }
}
