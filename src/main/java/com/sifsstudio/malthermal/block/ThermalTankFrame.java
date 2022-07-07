package com.sifsstudio.malthermal.block;

import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.capability.IMultiBlock;
import com.sifsstudio.malthermal.multiblock.MultiBlocks;
import com.sifsstudio.malthermal.tile.BaseTile;
import com.sifsstudio.malthermal.tile.ThermalTankFrameTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThermalTankFrame extends ContainerBlock {
    public ThermalTankFrame() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).isValidSpawn((state, world, pos, entity) -> false));
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader world) {
        return new ThermalTankFrameTile();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@Nonnull BlockState oldState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos);
            if (te != null) {
                te.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(component -> {
                    if (component.getId() != -1) {
                        world.getCapability(Capabilities.MULTI_BLOCK_CAPABILITY).ifPresent(multiBlockRecorder -> {
                            IMultiBlock.MultiBlock multiBlock = multiBlockRecorder.getMultiBlockById(component.getId());
                            if (!multiBlock.getType().isStructureValid(world, pos).isPresent()) {
                                multiBlock.getBlocks().forEach(posBlock -> {
                                    TileEntity posTe = world.getBlockEntity(posBlock);
                                    if (posTe != null) {
                                        posTe.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(posComponent -> {
                                            posComponent.setId(-1);
                                            BaseTile basePosTe = (BaseTile) posTe;
                                            basePosTe.notifyClient();
                                        });
                                    }
                                });
                                multiBlockRecorder.removeMultiBlock(multiBlock.getId());
                            } else {
                                multiBlock.getBlocks().remove(pos);
                            }
                        });
                    }
                });
            }
        }
        super.onRemove(oldState, world, pos, newState, isMoving);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(@Nonnull BlockState newState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            AtomicBoolean isAttachedToStructure = new AtomicBoolean(false);
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.relative(direction);
                TileEntity neighborTe = world.getBlockEntity(neighbor);
                if (neighborTe != null) {
                    neighborTe.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(mbComponent -> isAttachedToStructure.set(true));
                }
            }
            if (isAttachedToStructure.get()) {
                world.destroyBlock(pos, true);
            } else {
                MultiBlocks.THERMAL_TANK.get().isStructureValid(world, pos).ifPresent(result -> world.getCapability(Capabilities.MULTI_BLOCK_CAPABILITY).ifPresent(multiBlockRecorder -> multiBlockRecorder.addMultiBlock(result.controller, result.blocks, MultiBlocks.THERMAL_TANK.get())));
            }
        }
    }
}
