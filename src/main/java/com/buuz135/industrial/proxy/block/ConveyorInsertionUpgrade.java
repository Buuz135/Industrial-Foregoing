package com.buuz135.industrial.proxy.block;

import com.buuz135.industrial.api.conveyor.ConveyorUpgrade;
import com.buuz135.industrial.api.conveyor.ConveyorUpgradeFactory;
import com.buuz135.industrial.api.conveyor.IConveyorContainer;
import com.buuz135.industrial.utils.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ConveyorInsertionUpgrade extends ConveyorUpgrade {

    public ConveyorInsertionUpgrade(IConveyorContainer container, ConveyorUpgradeFactory factory, EnumFacing side) {
        super(container, factory, side);
    }

    public static Cuboid NORTHBB = new Cuboid(0.0625 * 2, 0, -0.0625 * 2, 0.0625 * 14, 0.0625 * 9, 0.0625 * 2,EnumFacing.NORTH.getIndex());
    public static Cuboid SOUTHBB = new Cuboid(0.0625 * 2, 0, 0.0625*14, 0.0625 * 14, 0.0625 * 9, 0.0625*18,EnumFacing.SOUTH.getIndex());
    public static Cuboid EASTBB = new Cuboid(0.0625*14, 0, 0.0625 * 2, 0.0625*18, 0.0625 * 9, 0.0625 * 14,EnumFacing.EAST.getIndex());
    public static Cuboid WESTBB = new Cuboid(-0.0625 * 2, 0, 0.0625 * 2, 0.0625 * 2, 0.0625 * 9, 0.0625 * 14,EnumFacing.WEST.getIndex());

    @Override
    public void handleEntity(Entity entity) {
        if (getWorld().isRemote)
            return;
        if (entity instanceof EntityItem) {
            TileEntity tile = getWorld().getTileEntity(getPos().offset(getSide()));
            if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getSide().getOpposite())) {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getSide().getOpposite());
                if (getBoundingBox().aabb().offset(getPos()).grow(0.1).intersects(entity.getEntityBoundingBox())) {
                    ItemStack stack = ((EntityItem) entity).getItem();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack remaining = handler.insertItem(i, stack, false);
                        if (remaining.isEmpty()) {
                            entity.setDead();
                            break;
                        } else {
                            ((EntityItem) entity).setItem(remaining);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Cuboid getBoundingBox() {
        switch (getSide()) {
            default:
            case NORTH:
                return NORTHBB;
            case SOUTH:
                return SOUTHBB;
            case EAST:
                return EASTBB;
            case WEST:
                return WESTBB;
        }
    }

    public static class Factory extends ConveyorUpgradeFactory {
        public Factory() {
            setRegistryName("insertion");
        }

        @Override
        public ConveyorUpgrade create(IConveyorContainer container, EnumFacing face) {
            return new ConveyorInsertionUpgrade(container, this, face);
        }

        @Override
        @Nonnull
        public ResourceLocation getModel(EnumFacing upgradeSide, EnumFacing conveyorFacing) {
            return new ResourceLocation(Reference.MOD_ID, "block/conveyor_upgrade_inserter_" + upgradeSide.getName().toLowerCase());
        }

        @Nonnull
        @Override
        public ResourceLocation getItemModel() {
            return new ResourceLocation(Reference.MOD_ID,"conveyor_insertion_upgrade");
        }
    }
}