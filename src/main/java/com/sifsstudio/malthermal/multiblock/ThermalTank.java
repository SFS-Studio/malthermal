package com.sifsstudio.malthermal.multiblock;

import com.sifsstudio.malthermal.block.AbstractThermalTankCore;
import com.sifsstudio.malthermal.block.ThermalGlass;
import com.sifsstudio.malthermal.block.ThermalTankFrame;
import com.sifsstudio.malthermal.registry.MultiBlockType;
import com.sifsstudio.malthermal.util.Utilities;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.*;

public class ThermalTank extends MultiBlockType {
    @Override
    public Optional<ScanResult> isStructureValid(World world, BlockPos trigger, Set<BlockPos> exclusive) {
        HashSet<BlockPos> scannedBlock = new HashSet<>();
        Queue<BlockPos> blockToScan = new LinkedList<>();
        blockToScan.add(trigger);
        BlockPos controller = null;
        while (!blockToScan.isEmpty()) {
            BlockPos next = blockToScan.remove();
            if (scannedBlock.contains(next)) {
                continue;
            }
            Block block = world.getBlockState(next).getBlock();
            if (block instanceof AbstractThermalTankCore && !exclusive.contains(next)) {
                if (controller == null) {
                    controller = next;
                } else {
                    return Optional.empty();
                }
            }
            scannedBlock.add(next);
            if (scannedBlock.size() > 10000) {
                return Optional.empty();
            }
            for (Direction direction : Direction.values()) {
                BlockPos relative = next.relative(direction);
                Block relativeBlock = world.getBlockState(relative).getBlock();
                if (!scannedBlock.contains(relative) && (relativeBlock instanceof AbstractThermalTankCore || relativeBlock instanceof ThermalTankFrame || relativeBlock instanceof ThermalGlass)) {
                    blockToScan.add(relative);
                }
            }
        }
        scannedBlock.removeAll(exclusive);
        if (controller == null) {
            return Optional.empty();
        }
        Utilities.Stats<BlockPos> stats = scannedBlock.stream().collect(Utilities.Stats.collector());
        Vector3i extent = stats.getMax().subtract(stats.getMin()).offset(1, 1, 1);
        if (stats.getCount() != 2 * ((extent.getX() - 2) * (extent.getY() - 2) + (extent.getY() - 2) * (extent.getZ() - 2) + (extent.getX() - 2) * (extent.getZ() - 2)) + 4 * (extent.getX() + extent.getY() + extent.getZ() - 4)) {
            return Optional.empty();
        }
        for (BlockPos pos : scannedBlock) {
            BlockPos min = stats.getMin();
            BlockPos max = stats.getMax();
            boolean isOnBottom = (pos.getY() == min.getY()) && Utilities.isInRect(pos.getX(), pos.getZ(), min.getX(), min.getZ(), max.getX(), max.getZ());
            boolean isOnTop = (pos.getY() == max.getY()) && Utilities.isInRect(pos.getX(), pos.getZ(), min.getX(), min.getZ(), max.getX(), max.getZ());
            boolean isOnFront = (pos.getZ() == min.getZ()) && Utilities.isInRect(pos.getX(), pos.getY(), min.getX(), min.getY(), max.getX(), max.getY());
            boolean isOnBack = (pos.getZ() == max.getZ()) && Utilities.isInRect(pos.getX(), pos.getY(), min.getX(), min.getY(), max.getX(), max.getY());
            boolean isOnLeft = (pos.getX() == min.getX()) && Utilities.isInRect(pos.getY(), pos.getZ(), min.getY(), min.getZ(), max.getY(), max.getZ());
            boolean isOnRight = (pos.getX() == max.getX()) && Utilities.isInRect(pos.getY(), pos.getZ(), min.getY(), min.getZ(), max.getY(), max.getZ());
            boolean isOnSurface = isOnBottom || isOnTop || isOnFront || isOnBack || isOnLeft || isOnRight;
            if (!isOnSurface) {
                return Optional.empty();
            }
        }

        return Optional.of(new ScanResult(new ArrayList<>(scannedBlock), extent, controller));
    }
}
