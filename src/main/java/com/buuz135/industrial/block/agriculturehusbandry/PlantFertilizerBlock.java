package com.buuz135.industrial.block.agriculturehusbandry;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.block.agriculturehusbandry.tile.PlantFertilizerTile;
import com.buuz135.industrial.module.ModuleAgricultureHusbandry;
import com.buuz135.industrial.utils.IndustrialTags;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PlantFertilizerBlock extends IndustrialBlock<PlantFertilizerTile> {

    public PlantFertilizerBlock() {
        super("plant_fertilizer", Properties.from(Blocks.IRON_BLOCK), PlantFertilizerTile.class, ModuleAgricultureHusbandry.TAB_AG_HUS);
    }

    @Override
    public IFactory<PlantFertilizerTile> getTileEntityFactory() {
        return PlantFertilizerTile::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public void registerRecipe(Consumer<IFinishedRecipe> consumer) {
        TitaniumShapedRecipeBuilder.shapedRecipe(this)
                .patternLine("PBP").patternLine("LML").patternLine("GRG")
                .key('P', IndustrialTags.Items.PLASTIC)
                .key('B', Items.GLASS_BOTTLE)
                .key('L', Items.LEATHER)
                .key('M', IndustrialTags.Items.MACHINE_FRAME_SIMPLE)
                .key('R', Items.REDSTONE)
                .key('G', ItemTags.makeWrapperTag("forge:gears/iron"))
                .build(consumer);
    }
}
