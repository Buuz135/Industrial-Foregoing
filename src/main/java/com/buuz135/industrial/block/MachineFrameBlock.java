package com.buuz135.industrial.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MachineFrameBlock extends BasicBlock {

    private MachineFrameItem item;
    private Rarity rarity;

    public MachineFrameBlock(Rarity rarity, ItemGroup group) {
        super(Properties.from(Blocks.IRON_BLOCK));
        this.setItemGroup(group);
        this.rarity = rarity;
    }

    @Override
    public Item asItem() {
        return item;
    }

    @Override
    public IFactory<BlockItem> getItemBlockFactory() {
        return () -> item = new MachineFrameItem(this, rarity, this.getItemGroup());
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }

    public class MachineFrameItem extends BlockItem {

        public MachineFrameItem(BasicBlock blockIn, Rarity rarity, ItemGroup group) {
            super(blockIn, new Item.Properties().group(group).rarity(rarity));
            this.setRegistryName(blockIn.getRegistryName());
        }

        @Override
        protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
            return false;
        }

        @Nullable
        @Override
        public String getCreatorModId(ItemStack itemStack) {
            return new TranslationTextComponent("itemGroup." + this.group.getPath()).getString();
        }

    }
}
