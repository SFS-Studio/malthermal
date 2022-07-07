package com.sifsstudio.malthermal.registry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;

public abstract class MultiBlockType<T> extends ForgeRegistryEntry<MultiBlockType<T>> {
    public abstract Optional<T> isStructureValid(World world, BlockPos trigger);
}
