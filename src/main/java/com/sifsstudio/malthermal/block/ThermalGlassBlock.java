package com.sifsstudio.malthermal.block;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThermalGlassBlock extends ContainerBlock {

    protected ThermalGlassBlock() {
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

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader world) {
        return new ThermalGlassTile();
    }
}
