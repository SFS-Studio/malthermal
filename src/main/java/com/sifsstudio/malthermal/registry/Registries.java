package com.sifsstudio.malthermal.registry;

import com.sifsstudio.malthermal.Malthermal;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class Registries {

    public static final DeferredRegister<MultiBlockType> MULTI_BLOCK_TYPE_DEFERRED_REGISTER = DeferredRegister.create(MultiBlockType.class, Malthermal.MOD_ID);

    public static final Supplier<IForgeRegistry<MultiBlockType>> MULTI_BLOCK_TYPE = MULTI_BLOCK_TYPE_DEFERRED_REGISTER.makeRegistry("multi_block_type", RegistryBuilder::new);

    public static void hook(IEventBus bus) {
        MULTI_BLOCK_TYPE_DEFERRED_REGISTER.register(bus);
    }

}
