package com.sifsstudio.malthermal;

import com.sifsstudio.malthermal.block.Blocks;
import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.multiblock.MultiBlocks;
import com.sifsstudio.malthermal.registry.Registries;
import com.sifsstudio.malthermal.tile.TileEntities;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Malthermal.MOD_ID)
public class Malthermal {

    public static final String MOD_ID = "malthermal";

    public Malthermal() {
        Blocks.hook(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntities.hook(FMLJavaModLoadingContext.get().getModEventBus());
        Registries.hook(FMLJavaModLoadingContext.get().getModEventBus());
        MultiBlocks.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Capabilities.register();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Blocks.THERMAL_GLASS.get(), RenderType.translucent());
    }
}
