package com.sifsstudio.malthermal.multiblock;

import com.sifsstudio.malthermal.registry.MultiBlockType;
import com.sifsstudio.malthermal.registry.Registries;
import net.minecraftforge.fml.RegistryObject;

public class MultiBlocks {

    public static final RegistryObject<MultiBlockType> THERMAL_TANK = Registries.MULTI_BLOCK_TYPE_DEFERRED_REGISTER.register("thermal_tank", ThermalTank::new);

    public static void init() {
        /* Place Holder */
    }

}
