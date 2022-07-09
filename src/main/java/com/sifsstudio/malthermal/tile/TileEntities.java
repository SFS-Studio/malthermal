package com.sifsstudio.malthermal.tile;

import com.sifsstudio.malthermal.Malthermal;
import com.sifsstudio.malthermal.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities {

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Malthermal.MOD_ID);

    public static final RegistryObject<TileEntityType<?>> IRON_THERMAL_TANK_CORE = TILE_ENTITIES.register("iron_thermal_tank_core", () -> TileEntityType.Builder.of(IronThermalTankCoreTile::new, Blocks.IRON_THERMAL_TANK_CORE.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> THERMAL_TANK_FRAME = TILE_ENTITIES.register("thermal_tank_frame", () -> TileEntityType.Builder.of(ThermalTankFrameTile::new, Blocks.THERMAL_TANK_FRAME.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> THERMAL_GLASS = TILE_ENTITIES.register("thermal_glass", () -> TileEntityType.Builder.of(ThermalGlassTile::new, Blocks.THERMAL_GLASS.get()).build(null));

    public static void hook(IEventBus bus) {
        TILE_ENTITIES.register(bus);
    }

}
