package com.buuz135.industrial.tile.misc;

import com.buuz135.industrial.proxy.client.infopiece.BlackHoleInfoPiece;
import com.buuz135.industrial.proxy.client.infopiece.IHasDisplayStack;
import com.buuz135.industrial.tile.CustomSidedTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.inventory.FluidTankType;
import net.ndrei.teslacorelib.items.TeslaWrench;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlackHoleTankTile extends CustomSidedTileEntity implements IHasDisplayStack {

    private IFluidTank tank;
    private boolean hadFluid;

    public BlackHoleTankTile() {
        super(BlackHoleTankTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        tank = this.addSimpleFluidTank(Integer.MAX_VALUE, "Tank", EnumDyeColor.CYAN, 6, 25, FluidTankType.BOTH, fluidStack -> true, fluidStack -> true);
    }

    @Override
    protected void innerUpdate() {
        if (hadFluid != tank.getFluidAmount() > 0) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            hadFluid = tank.getFluidAmount() > 0;
        }
    }

    @Override
    protected boolean supportsAddons() {
        return false;
    }

    @Override
    public ItemStack getItemStack() {
        if (tank.getFluid() == null || FluidUtil.getFilledBucket(tank.getFluid()).isEmpty())
            return new ItemStack(Items.BUCKET);
        return FluidUtil.getFilledBucket(tank.getFluid());
    }

    @Override
    public int getAmount() {
        return tank.getFluidAmount();
    }

    @Override
    public String getDisplayNameUnlocalized() {
        return tank.getFluid() == null ? "text.industrialforegoing.display.empty" : tank.getFluid().getLocalizedName();
    }

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
        List<IGuiContainerPiece> list = super.getGuiContainerPieces(container);
        list.add(new BlackHoleInfoPiece(this, 18 * 2 + 8, 25));
        return list;
    }

    public IFluidTank getTank() {
        return tank;
    }

    @Override
    public boolean getAllowRedstoneControl() {
        return false;
    }

    @Override
    protected boolean getShowPauseDrawerPiece() {
        return false;
    }

    @NotNull
    @Override
    public EnumActionResult onWrenchUse(@NotNull TeslaWrench wrench, @NotNull EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }
}
