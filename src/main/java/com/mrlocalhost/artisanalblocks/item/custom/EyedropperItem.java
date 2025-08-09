package com.mrlocalhost.artisanalblocks.item.custom;

import com.mrlocalhost.artisanalblocks.block.custom.ArtisanalBlock;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.component.ModDataComponentTypes;
import com.mrlocalhost.artisanalblocks.item.ModItems;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlockConfigs;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class EyedropperItem extends Item {

    public EyedropperItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
    @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        if (tooltipFlag.hasShiftDown()) {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item.shift_down_1").withStyle(ChatFormatting.AQUA));
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item.shift_down_2").withStyle(ChatFormatting.AQUA));
        } else if (tooltipFlag.hasControlDown() && hasAnyDataComponent(stack)){
            if (stack.has(ModDataComponentTypes.LIGHT_LEVEL)) {
                tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item.control_down_1").withStyle(ChatFormatting.RED)
                    .append(stack.get(ModDataComponentTypes.LIGHT_LEVEL).toString()));
            }
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item.control_down_2").withStyle(ChatFormatting.RED));
            tooltipAdder.addAll(getRestoneSettings(stack));
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item.control_down_3").withStyle(ChatFormatting.RED));
            tooltipAdder.addAll(getBlockFaceSettings(stack));
        } else {
            tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item_1").withStyle(ChatFormatting.YELLOW));
            if (hasAnyDataComponent(stack)) {
                tooltipAdder.add(Component.translatable("tooltip.artisanalblocks.eyedropper_item_2").withStyle(ChatFormatting.GREEN));
            }
        }
    }
    private List<Component> getRestoneSettings(ItemStack stack) {
        List<Component> components = new ArrayList<>();
        ModDataComponentTypes.REDSTONE_COMPONENT_STATES.forEach( state -> {
            String stateName = state.getRegisteredName().split(":")[1];
            stateName = stateName.substring(0, 1).toUpperCase()+stateName.substring(1);
            String text = " - "+stateName;
            if (stack.has(state)) {
                text+=(Boolean.TRUE.equals(stack.get(state))) ? "=High" : "=Low";
            } else {
                text+="=Ignored";
            }
            components.add(Component.literal(text));
        });
        return components;
    }
    private List<Component> getBlockFaceSettings(ItemStack stack) {
        List<Component> components = new ArrayList<>();
        List<ItemStack> itemStacks = null;
        if (stack.has(DataComponents.CONTAINER)) itemStacks = stack.get(DataComponents.CONTAINER).stream().toList();
        for (Direction direction : ArtisanalBlocksConstants.BLOCK_FACE_POS) {
            String text = " - "+ StringUtils.capitalize(direction.getName())+"=";
            if (itemStacks != null) {
                String itemName = itemStacks.get(direction.get3DDataValue()).getDisplayName().getString();
                text+=itemName.substring(1,itemName.length()-1);
            } else {
                text+="Empty";
            }
            components.add(Component.literal(text));
        }
        return components;
    }

    private boolean hasAnyDataComponent(ItemStack itemStack) {
        for (DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>> component : ModDataComponentTypes.DATA_COMPONENT_TYPES.getEntries()) {
            return itemStack.has(component);
        }
        return false;
    }
    private boolean hasBlocks(List<ItemStack> itemStacks) {
        for(ItemStack stack : itemStacks) {
            if (!stack.isEmpty() && !stack.is(Items.AIR)) {
                return true;
            }
        }
        return false;
    }

    private boolean doDataComponentsMatch(ItemStack stack1, ItemStack stack2) {
        if (!hasAnyDataComponent(stack1) && !hasAnyDataComponent(stack2)) {
            return true;
        }
        for (DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>> component : ModDataComponentTypes.DATA_COMPONENT_TYPES.getEntries()) {
            if (stack1.has(component) ^ stack2.has(component)) { //XOR presence of component
                return false;
            } else if (!Objects.equals(stack1.get(component), stack2.get(component))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        BlockState blockState = level.getBlockState(blockPos);
        if (!(level instanceof ServerLevel) || hand.equals(InteractionHand.OFF_HAND) || player == null) { // skip clientSide and off-hand
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(blockPos) instanceof ArtisanalBlockEntity artisanalBlockEntity) {
            if (!level.isClientSide()) {
                if (player.isCrouching()) { //copy
                    ItemStack initialItemStack = stack.copy();
                    int lightLevel = blockState.getValue(ArtisanalBlock.GLOW_VALUE);
                    if (lightLevel > 0) {
                        stack.set(ModDataComponentTypes.LIGHT_LEVEL, lightLevel);
                    } else {
                        stack.remove(ModDataComponentTypes.LIGHT_LEVEL);
                    }

                    int lightConfig = artisanalBlockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT).getStateInt();
                    int playerConfig = artisanalBlockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE).getStateInt();
                    int passiveConfig = artisanalBlockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE).getStateInt();
                    int hostileConfig = artisanalBlockEntity.getBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE).getStateInt();
                    if (lightConfig != 0) {
                        stack.set(ModDataComponentTypes.LIGHT_STATE, (lightConfig == 2));
                    } else {
                        stack.remove(ModDataComponentTypes.LIGHT_STATE);
                    }
                    if (playerConfig != 0) {
                        stack.set(ModDataComponentTypes.PLAYER_STATE, (playerConfig == 2));
                    } else {
                        stack.remove(ModDataComponentTypes.PLAYER_STATE);
                    }
                    if (passiveConfig != 0) {
                        stack.set(ModDataComponentTypes.PASSIVE_STATE, (passiveConfig == 2));
                    } else {
                        stack.remove(ModDataComponentTypes.PASSIVE_STATE);
                    }
                    if (hostileConfig != 0) {
                        stack.set(ModDataComponentTypes.HOSTILE_STATE, (hostileConfig == 2));
                    } else {
                        stack.remove(ModDataComponentTypes.HOSTILE_STATE);
                    }
                    List<ItemStack> itemStacks = new ArrayList<>();
                    for (Direction direction: ArtisanalBlocksConstants.BLOCK_FACE_POS) {
                        itemStacks.add(artisanalBlockEntity.inventory.getStackInSlot(direction.get3DDataValue()).copy());
                    }
                    if (hasBlocks(itemStacks)) {
                        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(itemStacks));
                    } else {
                        stack.remove(DataComponents.CONTAINER);
                    }
                    player.setItemInHand(hand, stack);
                    if (hasAnyDataComponent(stack)) {
                        if (!doDataComponentsMatch(stack, initialItemStack)) {
                            level.playSound(null, blockPos, SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 0.5F, 0.75F);
                        }
                    } else {
                        level.playSound(null, blockPos, SoundEvents.CREEPER_DEATH, SoundSource.BLOCKS, 0.5F, 0.75F);
                    }
                } else { //paste
                    //set block information
                    if (stack.has(ModDataComponentTypes.LIGHT_LEVEL)) {
                        blockState = blockState.setValue(ArtisanalBlock.GLOW_VALUE, stack.get(ModDataComponentTypes.LIGHT_LEVEL));
                        level.setBlockAndUpdate(blockPos, blockState);
                    }
                    if (stack.has(ModDataComponentTypes.LIGHT_STATE)) {
                        if (stack.get(ModDataComponentTypes.LIGHT_STATE)) { //HIGH
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT, ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH);
                        } else { //LOW
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT, ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW);
                        }
                    } else {
                        artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.LIGHT, ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED);
                    }
                    if (stack.has(ModDataComponentTypes.PLAYER_STATE)) {
                        if (stack.get(ModDataComponentTypes.PLAYER_STATE)) { //HIGH
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH);
                        } else { //LOW
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW);
                        }
                    } else {
                        artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PLAYER_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED);
                    }
                    if (stack.has(ModDataComponentTypes.PASSIVE_STATE)) {
                        if (stack.get(ModDataComponentTypes.PASSIVE_STATE)) { //HIGH
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH);
                        } else { //LOW
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW);
                        }
                    } else {
                        artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.PASSIVE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED);
                    }
                    if (stack.has(ModDataComponentTypes.HOSTILE_STATE)) {
                        if (stack.get(ModDataComponentTypes.HOSTILE_STATE)) { //HIGH
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.HIGH);
                        } else { //LOW
                            artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.LOW);
                        }
                    } else {
                        artisanalBlockEntity.setBlockConfig(ArtisanalBlockConfigs.BLOCK_OPTIONS.HOSTILE_PASSAGE, ArtisanalBlockConfigs.REDSTONE_OPTIONS.IGNORED);
                    }
                    if (stack.has(DataComponents.CONTAINER)) {
                        List<ItemStack> itemStacks = stack.get(DataComponents.CONTAINER).stream().toList();
                        for (Direction direction : ArtisanalBlocksConstants.BLOCK_FACE_POS) {
                            artisanalBlockEntity.inventory.setStackInSlot(direction.get3DDataValue(), itemStacks.get(direction.get3DDataValue()));
                        }
                    }
                    level.setBlockEntity(artisanalBlockEntity);
                    ((ArtisanalBlock) level.getBlockState(blockPos).getBlock()).updateRedstoneStates(blockState, level, blockPos);
                    level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.5F, 0.75F);
                }
            }

            return InteractionResult.CONSUME;
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }

    @Override //use on air
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!player.isCrouching() || level.isClientSide || usedHand.equals(InteractionHand.OFF_HAND)) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (itemStack.getItem().equals(ModItems.EYEDROPPER.get())) {
            boolean removedComponent = false;
            for (DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>> component : ModDataComponentTypes.DATA_COMPONENT_TYPES.getEntries()) {
                if (itemStack.has(component)) {
                    itemStack.remove(component);
                    removedComponent = true;
                }
            }
            if (itemStack.has(DataComponents.CONTAINER)) {
                itemStack.remove(DataComponents.CONTAINER);
                removedComponent = true;
            }
            if (removedComponent) {
                level.playSound(null, player.getOnPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }


        return InteractionResultHolder.consume(itemStack);
    }
}
