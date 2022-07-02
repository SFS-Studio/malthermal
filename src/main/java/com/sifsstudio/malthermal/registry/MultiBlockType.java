package com.sifsstudio.malthermal.registry;

import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class MultiBlockType extends ForgeRegistryEntry<MultiBlockType> {
    public abstract boolean isStructureValid();
}
