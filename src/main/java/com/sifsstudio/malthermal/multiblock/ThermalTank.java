package com.sifsstudio.malthermal.multiblock;

import com.sifsstudio.malthermal.registry.MultiBlockType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ThermalTank extends MultiBlockType {
    @Override
    public Optional<ScanResult> isStructureValid(World world, BlockPos trigger) {
        return Optional.empty();
    }
}
