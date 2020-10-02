/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2019, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.proxy;

import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.proxy.event.FakePlayerRideEntityHandler;
import com.buuz135.industrial.proxy.event.MeatFeederTickHandler;
import com.buuz135.industrial.proxy.event.MobDeathHandler;
import com.buuz135.industrial.proxy.event.SkullHandler;
import com.buuz135.industrial.utils.CraftingUtils;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.apihandlers.RecipeHandlers;
import com.google.gson.JsonParser;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonProxy {

    public static final String CONTRIBUTORS_FILE = "https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json";
    public static Random random;
    public static List<String> CONTRIBUTORS = new ArrayList<>();

    public static DamageSource custom = new DamageSource("if_custom") {
        @Override
        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslationTextComponent("text.industrialforegoing.chat.slaughter_kill", entityLivingBaseIn.getDisplayName(), TextFormatting.RESET);
        }
    };
    public static ResourceLocation PINK_SLIME_LOOT;

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public void run() {
        LaserDrillEntry.addOreFile(new ResourceLocation(Reference.MOD_ID, "default_ores.json"));

        //CraftingHelper.register(new ResourceLocation(Reference.MOD_ID, "configuration_value"), new ConfigurationConditionFactory());
        random = new Random();

        MinecraftForge.EVENT_BUS.register(new StrawRegistry());
        MinecraftForge.EVENT_BUS.register(new MobDeathHandler());
        MinecraftForge.EVENT_BUS.register(new FakePlayerRideEntityHandler());
        MinecraftForge.EVENT_BUS.register(new SkullHandler());

        EventManager.forge(LivingEvent.LivingUpdateEvent.class).process(MeatFeederTickHandler::onTick).subscribe();

//        CustomConfiguration.config = new Configuration(event.getSuggestedConfigurationFile());
//        CustomConfiguration.config.load();
//        CustomConfiguration.sync();
//        CustomConfiguration.configValues = new HashMap<>();
//        CustomConfiguration.configValues.put("useTEFrames", CustomConfiguration.config.getBoolean("useTEFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using Thermal Expansion frames"));
//        CustomConfiguration.configValues.put("useEnderIOFrames", CustomConfiguration.config.getBoolean("useEnderIOFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using EnderIO frames"));
//        CustomConfiguration.configValues.put("useOriginalFrames", CustomConfiguration.config.getBoolean("useOriginalFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using TeslaCoreLib frames"));
//        CustomConfiguration.configValues.put("useMekanismFrames", CustomConfiguration.config.getBoolean("useMekanismFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using Mekanism Steel Casing"));
//        CustomConfiguration.configValues.put("machines.wither_builder.HCWither", CustomConfiguration.config.getBoolean("HCWither", "machines.wither_builder", false, "If enabled, only the wither builder will be able to place wither skulls. That means that players won't be able to place wither skulls. The recipe will change, but that will need a restart."));
//
//        if (Loader.isModLoaded("crafttweaker")) CraftTweakerHelper.register();
//        if (Loader.isModLoaded("baubles")) MinecraftForge.EVENT_BUS.register(new MeatFeederBauble.Event());

        //EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "pink_slime"), EntityPinkSlime.class, "pink_slime", 135135, IndustrialForegoing.instance, 32, 1, false, 10485860, 16777215);
        //PINK_SLIME_LOOT = LootTables.register(new ResourceLocation(Reference.MOD_ID, "entities/pink_slime"));TODO REGISTER

        try {
            new JsonParser().parse(readUrl(CONTRIBUTORS_FILE)).getAsJsonObject().get("uuid").getAsJsonArray().forEach(jsonElement -> CONTRIBUTORS.add(jsonElement.getAsString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecipeHandlers.loadBioReactorEntries();
        RecipeHandlers.loadSludgeRefinerEntries();
        RecipeHandlers.loadProteinReactorEntries();
        RecipeHandlers.loadFluidDictionaryEntries();
        RecipeHandlers.loadWoodToLatexEntries();
        RecipeHandlers.loadOreEntries();

        CraftingUtils.generateCrushedRecipes();
        RecipeHandlers.executeCraftweakerActions();
        //LaserDrillEntry.loadLaserConfigs(configFolder);

    }

}
