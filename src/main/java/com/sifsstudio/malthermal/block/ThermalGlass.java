package com.sifsstudio.malthermal.block;

import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.capability.IMultiBlock;
import com.sifsstudio.malthermal.multiblock.MultiBlocks;
import com.sifsstudio.malthermal.tile.BaseTile;
import com.sifsstudio.malthermal.tile.ThermalGlassTile;
import com.sifsstudio.malthermal.util.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThermalGlass extends ContainerBlock {

    protected ThermalGlass() {
        super(AbstractBlock.Properties.of(Material.GLASS).sound(SoundType.GLASS).noOcclusion().isValidSpawn(Utilities::neverSpawn).isRedstoneConductor(Utilities::neverDo).isSuffocating(Utilities::neverDo).isViewBlocking(Utilities::neverDo));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getVisualShape(@Nonnull BlockState pState, @Nonnull IBlockReader pReader, @Nonnull BlockPos pPos, @Nonnull ISelectionContext pContext) {
        return VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(@Nonnull BlockState pState, @Nonnull IBlockReader pLevel, @Nonnull BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(@Nonnull BlockState pState, @Nonnull IBlockReader pReader, @Nonnull BlockPos pPos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(@Nonnull BlockState pState, @Nonnull BlockState pAdjacentBlockState, @Nonnull Direction pSide) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
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
                            Set<BlockPos> exclusive = new HashSet<>();
                            exclusive.add(pos);
                            if (!multiBlock.getType().isStructureValid(world, pos, exclusive).isPresent()) {
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

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader world) {
        return new ThermalGlassTile();
    }
}
