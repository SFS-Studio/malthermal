package com.sifsstudio.malthermal;

import com.sifsstudio.malthermal.block.Blocks;
import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.multiblock.MultiBlocks;
import com.sifsstudio.malthermal.registry.Registries;
import com.sifsstudio.malthermal.tile.TileEntities;
import net.minecraftforge.fml.common.Mod;
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
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Capabilities.register();
    }
}
