package com.buuz135.industrial.tile.block;

import com.buuz135.industrial.book.BookCategory;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.misc.BlackHoleTankTile;
import com.buuz135.industrial.utils.RecipeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ndrei.teslacorelib.items.MachineCaseItem;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class BlackHoleTankBlock extends CustomOrientedBlock<BlackHoleTankTile> {

    public BlackHoleTankBlock() {
        super("black_hole_tank", BlackHoleTankTile.class);
    }

    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this), "ppp", "eae", "cmc",
                'p', ItemRegistry.plastic,
                'e', Items.ENDER_EYE,
                'a', Items.ENDER_PEARL,
                'c', Items.BUCKET,
                'm', MachineCaseItem.INSTANCE);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) instanceof BlackHoleTankTile) {
            BlackHoleTankTile tile = (BlackHoleTankTile) world.getTileEntity(pos);
            ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
            if (tile.getAmount() > 0) {
                if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
                if (tile.getTank().getFluid() != null) {
                    stack.setTagCompound(tile.getTank().getFluid().writeToNBT(new NBTTagCompound()));
                }
            }
            float f = 0.7F;
            float d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
            entityitem.setDefaultPickupDelay();
            if (stack.hasTagCompound()) {
                entityitem.getItem().setTagCompound(stack.getTagCompound().copy());
            }
            world.spawnEntity(entityitem);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Arrays.asList();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (stack.hasTagCompound() && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof BlackHoleTankTile && FluidRegistry.isFluidRegistered(stack.getTagCompound().getString("FluidName"))) {
            BlackHoleTankTile tile = (BlackHoleTankTile) world.getTileEntity(pos);
            tile.getTank().fill(new FluidStack(FluidRegistry.getFluid(stack.getTagCompound().getString("FluidName")), stack.getTagCompound().getInteger("Amount")), true);
        }
    }

    @Override
    public List<String> getTooltip(ItemStack stack) {
        List<String> tooltip = super.getTooltip(stack);
        if (stack.hasTagCompound() && FluidRegistry.isFluidRegistered(stack.getTagCompound().getString("FluidName"))) {
            tooltip.add(new TextComponentTranslation("text.industrialforegoing.display.fluid").getUnformattedText() + " " + new TextComponentTranslation(FluidRegistry.getFluid(stack.getTagCompound().getString("FluidName")).getUnlocalizedName()).getUnformattedText());
            tooltip.add(new TextComponentTranslation("text.industrialforegoing.display.amount").getUnformattedText() + " " + stack.getTagCompound().getInteger("Amount"));
        }
        return tooltip;
    }

    @Override
    public BookCategory getCategory() {
        return BookCategory.STORAGE;
    }

    @Override
    public boolean onBlockActivated(@Nullable World worldIn, @Nullable BlockPos pos, @Nullable IBlockState state, @Nullable EntityPlayer playerIn, @Nullable EnumHand hand, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn != null && hand != null && worldIn != null && pos != null) {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (!stack.isEmpty() && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && worldIn.getTileEntity(pos) instanceof BlackHoleTankTile) {
                BlackHoleTankTile tile = (BlackHoleTankTile) worldIn.getTileEntity(pos);
                FluidActionResult result = FluidUtil.tryFillContainer(stack, (IFluidHandler) tile.getTank(), Integer.MAX_VALUE, playerIn, true);
                if (result.isSuccess()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        playerIn.setHeldItem(hand, result.result);
                    } else {
                        playerIn.addItemStackToInventory(result.result);
                    }
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
