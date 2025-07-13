package com.mrlocalhost.artisanalblocks.screen.custom;

import com.mrlocalhost.artisanalblocks.block.ModBlocks;
import com.mrlocalhost.artisanalblocks.block.entity.ArtisanalBlockEntity;
import com.mrlocalhost.artisanalblocks.screen.ModMenuTypes;
import com.mrlocalhost.artisanalblocks.utils.ArtisanalBlocksUtilities;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArtisanalBlockMenu extends AbstractContainerMenu {

    public final ArtisanalBlockEntity blockEntity;
    private final Level level;
    private List<Integer> CUSTOM_SLOTS;

    @SuppressWarnings("all") //suppressed 'try'-with-resources statement warning
    public ArtisanalBlockMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ArtisanalBlockMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.ARTISANAL_BLOCK_MENU.get(), containerId);
        this.blockEntity = ((ArtisanalBlockEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addArtisanalBlockSlots();
    }

    private void addArtisanalBlockSlots() {
        CUSTOM_SLOTS = List.of(
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.DOWN.get3DDataValue(), 8, 16)).index,
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.UP.get3DDataValue(), 54, 16)).index,
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.NORTH.get3DDataValue(), 8, 34)).index,
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.SOUTH.get3DDataValue(), 54, 34)).index,
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.WEST.get3DDataValue(), 8, 52)).index,
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, Direction.EAST.get3DDataValue(), 54, 52)).index
        );
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
            player, ModBlocks.ARTISANAL_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override @SuppressWarnings("all") //suppressing whining about how a conditional is "always true" when it is not
    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        try {
            if (!this.CUSTOM_SLOTS.contains(slotId)) { //if not one of the custom GUI slots
                super.clicked(slotId, button, clickType, player);
                return;
            }
            ItemStack itemCopy = this.getCarried().copy();
            itemCopy.setCount(1);
            if (itemCopy.isEmpty() || ArtisanalBlocksUtilities.isPlacableInArtisanalBlock(itemCopy)) {
                if (itemCopy.getItem().equals(ModBlocks.ARTISANAL_BLOCK.asItem())) {
                    this.setItem(slotId, 0, ItemStack.EMPTY);
                } else {
                    this.setItem(slotId, 0, itemCopy);
                }
            }
        } catch (Exception exception) {
            CrashReport crashreport = CrashReport.forThrowable(exception, "Container click");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Click info");
            crashreportcategory.setDetail("Menu Type", () -> this.getType() != null ? BuiltInRegistries.MENU.getKey(this.getType()).toString() : "<no type>");
            crashreportcategory.setDetail("Menu Class", () -> this.getClass().getCanonicalName());
            crashreportcategory.setDetail("Slot Count", this.slots.size());
            crashreportcategory.setDetail("Slot", slotId);
            crashreportcategory.setDetail("Button", button);
            crashreportcategory.setDetail("Type", clickType);
            throw new ReportedException(crashreport);
        }
    }
}
