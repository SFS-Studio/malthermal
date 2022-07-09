package com.sifsstudio.malthermal.registry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class MultiBlockType extends ForgeRegistryEntry<MultiBlockType> {
    public abstract Optional<ScanResult> isStructureValid(World world, BlockPos trigger, Set<BlockPos> exclusive);

    public static class ScanResult {
        public final List<BlockPos> blocks;
        public final Vector3i extent;
        public final BlockPos controller;

        public ScanResult(List<BlockPos> blocks, Vector3i extent, BlockPos controller) {
            this.blocks = blocks;
            this.extent = extent;
            this.controller = controller;
        }

        public int getVolume() {
            return this.extent.getX() * this.extent.getY() * this.extent.getZ();
        }
    }
}
