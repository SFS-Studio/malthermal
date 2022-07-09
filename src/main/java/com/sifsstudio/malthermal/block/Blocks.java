package com.sifsstudio.malthermal.block;

import com.sifsstudio.malthermal.Malthermal;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class Blocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Malthermal.MOD_ID);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Malthermal.MOD_ID);

    public static final RegistryObject<Block> THERMAL_TANK_FRAME = registerWithItem("thermal_tank_frame", ThermalTankFrame::new);
    public static final RegistryObject<Block> IRON_THERMAL_TANK_CORE = registerWithItem("iron_thermal_tank_core", IronThermalTankCore::new);
    public static final RegistryObject<Block> THERMAL_GLASS = registerWithItem("thermal_glass", ThermalGlass::new);

    public static void hook(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
    }

    private static <I extends Block> RegistryObject<Block> registerWithItem(String name, Supplier<? extends I> supplier) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static <I extends Block> RegistryObject<Block> registerWithItem(String name, Supplier<? extends I> supplier, Item.Properties props) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), props));
        return block;
    }

}
