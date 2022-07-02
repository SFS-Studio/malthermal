package com.sifsstudio.malthermal.event;

import com.sifsstudio.malthermal.Malthermal;
import com.sifsstudio.malthermal.capability.CapabilityMultiBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Malthermal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {
    @SubscribeEvent
    static void attachWorldCapabilities(AttachCapabilitiesEvent<World> event) {
        event.addCapability(new ResourceLocation(Malthermal.MOD_ID, "multi_blocks"), new CapabilityMultiBlock.WorldCapabilityProvider());
    }
}
