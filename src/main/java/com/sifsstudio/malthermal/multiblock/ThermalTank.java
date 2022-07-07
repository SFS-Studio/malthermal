package com.sifsstudio.malthermal.multiblock;

import com.sifsstudio.malthermal.registry.MultiBlockType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class ThermalTank extends MultiBlockType<ThermalTank.ScanResult> {
    @Override
    public Optional<ScanResult> isStructureValid(World world, BlockPos trigger) {
        return Optional.empty();
    }

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
