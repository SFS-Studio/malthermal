package com.sifsstudio.malthermal.block;

import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.capability.IMultiBlock;
import com.sifsstudio.malthermal.multiblock.MultiBlocks;
import com.sifsstudio.malthermal.tile.BaseTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractThermalTankCore extends ContainerBlock {

    protected AbstractThermalTankCore(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState pState) {
        return BlockRenderType.MODEL;
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
                            multiBlock.getBlocks().forEach(posBlock -> {
                                TileEntity posTe = world.getBlockEntity(posBlock);
                                if (posTe != null) {
                                    posTe.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(posComponent -> {
                                        posComponent.setId(-1);
                                        BaseTile basePosTe = (BaseTile) posTe;
                                        basePosTe.notifyChanged();
                                    });
                                }
                            });
                            multiBlockRecorder.removeMultiBlock(multiBlock.getId());
                        });
                    }
                });
            }
        }
        super.onRemove(oldState, world, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity pPlacer, @Nonnull ItemStack stack) {
        if (!world.isClientSide) {
            AtomicBoolean isAttachedToStructure = new AtomicBoolean(false);
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.relative(direction);
                TileEntity neighborTe = world.getBlockEntity(neighbor);
                if (neighborTe != null) {
                    neighborTe.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(mbComponent -> {
                        if (mbComponent.getId() != -1) {
                            isAttachedToStructure.set(true);
                        }
                    });
                }
            }
            if (isAttachedToStructure.get()) {
                world.destroyBlock(pos, true);
            } else {
                MultiBlocks.THERMAL_TANK.get().isStructureValid(world, pos, new HashSet<>()).ifPresent(result -> world.getCapability(Capabilities.MULTI_BLOCK_CAPABILITY).ifPresent(multiBlockRecorder -> {
                    int id = multiBlockRecorder.addMultiBlock(result.controller, result.blocks, MultiBlocks.THERMAL_TANK.get());
                    result.blocks.forEach(blockPos -> {
                        BaseTile tile = (BaseTile) world.getBlockEntity(blockPos);
                        if (tile != null) {
                            tile.getCapability(Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY).ifPresent(multiBlockComponent -> multiBlockComponent.setId(id));
                            tile.notifyChanged();
                        }
                    });
                }));
            }
        }
    }
}
